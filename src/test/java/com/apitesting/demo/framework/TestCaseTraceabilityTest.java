package com.apitesting.demo.framework;

import com.apitesting.demo.tests.PostsAPITest;
import com.apitesting.demo.tests.CommentsAPITest;
import com.apitesting.demo.tests.AlbumsAPITest;
import com.apitesting.demo.tests.PhotosAPITest;
import com.apitesting.demo.tests.TodosAPITest;
import com.apitesting.demo.tests.UsersAPITest;
import com.apitesting.demo.tests.NetworkMockingTest;
import io.qameta.allure.AllureId;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Enforces sync between documented test cases and automated tests.
 * This is a framework validation tool, not a functional test.
 */
public class TestCaseTraceabilityTest {

    private static final String TEST_CASES_DOC_RESOURCE = "TEST_CASES_DOCUMENTATION.csv";

    private static final Class<?>[] TRACKED_TEST_CLASSES = new Class<?>[] {
            PostsAPITest.class,
            CommentsAPITest.class,
            AlbumsAPITest.class,
            PhotosAPITest.class,
            TodosAPITest.class,
            UsersAPITest.class,
            NetworkMockingTest.class
    };

    @Test
    public void testDocumentationAndAutomationStayInSync() {
        Map<String, String> documentationMapping = loadDocumentationMapping();
        Map<String, String> automatedMapping = collectAutomatedMapping();

        List<String> missingInDocumentation = new ArrayList<>();
        for (String automatedKey : automatedMapping.keySet()) {
            if (!documentationMapping.containsKey(automatedKey)) {
                missingInDocumentation.add(automatedKey + " -> " + automatedMapping.get(automatedKey));
            }
        }

        List<String> missingInAutomation = new ArrayList<>();
        for (String documentationKey : documentationMapping.keySet()) {
            if (!automatedMapping.containsKey(documentationKey)) {
                missingInAutomation.add(documentationKey + " -> " + documentationMapping.get(documentationKey));
            }
        }

        Collections.sort(missingInDocumentation);
        Collections.sort(missingInAutomation);

        Assert.assertTrue(missingInDocumentation.isEmpty(),
                "Document missing test methods:\n" + String.join("\n", missingInDocumentation));

        Assert.assertTrue(missingInAutomation.isEmpty(),
                "Automated suite missing documented methods:\n" + String.join("\n", missingInAutomation));
    }

    @Test
    public void testAllureIdMatchesDocumentationWhenPresent() {
        Map<String, String> documentationMapping = loadDocumentationMapping();
        List<String> mismatches = new ArrayList<>();

        for (Class<?> testClass : TRACKED_TEST_CLASSES) {
            for (Method method : testClass.getMethods()) {
                if (method.getAnnotation(Test.class) == null) {
                    continue;
                }

                AllureId allureId = method.getAnnotation(AllureId.class);
                if (allureId == null || allureId.value() == null || allureId.value().trim().isEmpty()) {
                    continue;
                }

                String key = testClass.getSimpleName() + "#" + method.getName();
                String expected = documentationMapping.get(key);
                String actual = allureId.value().trim();
                if (expected != null && !expected.equals(actual)) {
                    mismatches.add(key + " expected " + expected + " but found @AllureId(" + actual + ")");
                }
            }
        }

        Collections.sort(mismatches);
        Assert.assertTrue(mismatches.isEmpty(),
                "@AllureId mismatches found:\n" + String.join("\n", mismatches));
    }

    private static Map<String, String> collectAutomatedMapping() {
        Map<String, String> mapping = new HashMap<>();
        Set<String> duplicateKeys = new HashSet<>();

        for (Class<?> testClass : TRACKED_TEST_CLASSES) {
            for (Method method : testClass.getMethods()) {
                if (method.getAnnotation(Test.class) == null) {
                    continue;
                }

                String key = testClass.getSimpleName() + "#" + method.getName();
                String id = resolveId(method, key);
                if (mapping.containsKey(key)) {
                    duplicateKeys.add(key);
                }
                mapping.put(key, id);
            }
        }

        if (!duplicateKeys.isEmpty()) {
            List<String> sorted = new ArrayList<>(duplicateKeys);
            Collections.sort(sorted);
            Assert.fail("Duplicate test keys found: " + Arrays.toString(sorted.toArray()));
        }

        return mapping;
    }

    private static String resolveId(Method method, String key) {
        AllureId allureId = method.getAnnotation(AllureId.class);
        if (allureId != null && allureId.value() != null && !allureId.value().trim().isEmpty()) {
            return allureId.value().trim();
        }
        return "NO-ANNOTATION-ID@" + key;
    }

    private static Map<String, String> loadDocumentationMapping() {
        Map<String, String> csvMethodToId = new HashMap<>();

        try (InputStream inputStream = TestCaseTraceabilityTest.class.getClassLoader()
                .getResourceAsStream(TEST_CASES_DOC_RESOURCE)) {
            if (inputStream == null) {
                Assert.fail("Missing resource: " + TEST_CASES_DOC_RESOURCE);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String headerLine = reader.readLine();
                if (headerLine == null) {
                    return Collections.emptyMap();
                }

                List<String> headers = parseCsvLine(headerLine);
                int tcIdIndex = indexOfHeader(headers, "TC ID");
                int methodIndex = indexOfHeader(headers, "Method");
                if (tcIdIndex < 0 || methodIndex < 0) {
                    return Collections.emptyMap();
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    List<String> fields = parseCsvLine(line);
                    if (fields.size() <= Math.max(tcIdIndex, methodIndex)) {
                        continue;
                    }

                    String testCaseId = fields.get(tcIdIndex).trim();
                    String methodName = fields.get(methodIndex).trim();
                    if (testCaseId.isEmpty() || methodName.isEmpty()) {
                        continue;
                    }

                    csvMethodToId.put(methodName, testCaseId);
                }
            }
        } catch (IOException e) {
            Assert.fail("Unable to load test case documentation: " + e.getMessage());
        }

        Map<String, String> mapping = new HashMap<>();
        for (Class<?> testClass : TRACKED_TEST_CLASSES) {
            for (Method method : testClass.getMethods()) {
                if (method.getAnnotation(Test.class) == null) {
                    continue;
                }

                String methodName = method.getName();
                String testCaseId = csvMethodToId.get(methodName);
                if (testCaseId != null && !testCaseId.isEmpty()) {
                    mapping.put(testClass.getSimpleName() + "#" + methodName, testCaseId);
                }
            }
        }

        return mapping;
    }

    private static int indexOfHeader(List<String> headers, String expectedHeader) {
        for (int i = 0; i < headers.size(); i++) {
            if (expectedHeader.equalsIgnoreCase(headers.get(i).trim())) {
                return i;
            }
        }
        return -1;
    }

    private static List<String> parseCsvLine(String line) {
        ArrayList<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        values.add(current.toString());
        return values;
    }
}


