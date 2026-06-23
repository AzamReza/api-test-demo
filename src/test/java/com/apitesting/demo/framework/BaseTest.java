package com.apitesting.demo.framework;

import com.apitesting.demo.utils.APIConstants;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Base Test Class - Provides common setup for all test classes
 */
public class BaseTest {

    private static final Logger TEST_LOGGER = Logger.getLogger("ApiTestExecutionLogger");
    private static final String DEFAULT_LOG_FILE = "target/test-execution.log";
    private static final String TEST_LOG_FILE_PROPERTY = "test.log.file";
    private static final String TEST_CASES_DOC_RESOURCE = "TEST_CASES_DOCUMENTATION.csv";

    private static final Map<String, String> TEST_CASE_ID_BY_CLASS_AND_METHOD = loadTestCaseIds();
    private static final ThreadLocal<String> CURRENT_TEST_CASE_ID = new ThreadLocal<>();
    private static boolean loggerConfigured = false;

    private static synchronized void configureLogger() {
        if (loggerConfigured) {
            return;
        }

        try {
            String logFilePath = System.getProperty(TEST_LOG_FILE_PROPERTY, DEFAULT_LOG_FILE);
            Path resolvedPath = Paths.get(logFilePath);
            Path parentPath = resolvedPath.getParent();
            if (parentPath != null) {
                Files.createDirectories(parentPath);
            }

            FileHandler fileHandler = new FileHandler(resolvedPath.toString(), true);
            fileHandler.setLevel(Level.INFO);
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("%1$tF %1$tT [%2$s] %3$s%n",
                            record.getMillis(),
                            record.getLevel().getName(),
                            record.getMessage());
                }
            });

            TEST_LOGGER.setUseParentHandlers(false);
            TEST_LOGGER.setLevel(Level.INFO);
            TEST_LOGGER.addHandler(fileHandler);
            loggerConfigured = true;
        } catch (IOException e) {
            throw new RuntimeException("Unable to configure test logger", e);
        }
    }

    public static void logTest(String message) {
        configureLogger();
        String testCaseId = CURRENT_TEST_CASE_ID.get();
        String messageWithId = (testCaseId == null || testCaseId.isEmpty()) ? message : "[" + testCaseId + "] " + message;
        TEST_LOGGER.info(messageWithId);
        Allure.step(messageWithId);
    }

    private static Map<String, String> loadTestCaseIds() {
        Map<String, String> mapping = new HashMap<>();

        try (InputStream inputStream = BaseTest.class.getClassLoader().getResourceAsStream(TEST_CASES_DOC_RESOURCE)) {
            if (inputStream == null) {
                return Collections.emptyMap();
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

                    // CSV is method-based, so map directly by method name.
                    mapping.put(methodName, testCaseId);
                }
            }
        } catch (IOException ignored) {
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(mapping);
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
        java.util.ArrayList<String> values = new java.util.ArrayList<>();
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

    private static String resolveTestCaseId(String className, String methodName) {
        String classSpecific = TEST_CASE_ID_BY_CLASS_AND_METHOD.get(className + "#" + methodName);
        if (classSpecific != null && !classSpecific.isEmpty()) {
            return classSpecific;
        }
        return TEST_CASE_ID_BY_CLASS_AND_METHOD.get(methodName);
    }

    private String resolveEffectiveTestCaseId(Method testMethod, String className, String methodName) {
        AllureId allureId = testMethod.getAnnotation(AllureId.class);
        String annotationId = allureId == null ? null : allureId.value();
        if (annotationId != null) {
            annotationId = annotationId.trim();
        }

        String documentationId = resolveTestCaseId(className, methodName);
        if (annotationId != null && !annotationId.isEmpty()) {
            if (documentationId != null && !documentationId.equals(annotationId)) {
                logTest("Warning: @AllureId " + annotationId + " does not match documentation ID " + documentationId
                        + " for " + className + "#" + methodName);
            }
            return annotationId;
        }

        return documentationId;
    }

    private static String toReadableTestName(String methodName) {
        String normalized = methodName;
        if (normalized.startsWith("test") && normalized.length() > 4) {
            normalized = normalized.substring(4);
        }
        String spaced = normalized.replaceAll("([a-z])([A-Z])", "$1 $2").trim();
        if (spaced.isEmpty()) {
            return methodName;
        }
        return Character.toUpperCase(spaced.charAt(0)) + spaced.substring(1);
    }

    @BeforeMethod
    public void configureAllureTestCaseMetadata(Method testMethod) {
        String methodName = testMethod.getName();
        String className = this.getClass().getSimpleName();

        String testCaseId = resolveEffectiveTestCaseId(testMethod, className, methodName);
        CURRENT_TEST_CASE_ID.set(testCaseId);
        Description description = testMethod.getAnnotation(Description.class);
        String baseDisplayName;

        if (description != null && description.value() != null && !description.value().trim().isEmpty()) {
            baseDisplayName = description.value().trim();
        } else {
            baseDisplayName = toReadableTestName(methodName);
        }

        String displayName = (testCaseId == null || testCaseId.isEmpty())
                ? baseDisplayName
                : testCaseId + " - " + baseDisplayName;

        Allure.getLifecycle().updateTestCase(result -> {
            result.setName(displayName);
            result.setFullName(className + "." + methodName);
        });

        if (testCaseId != null && !testCaseId.isEmpty()) {
            Allure.label("testCaseId", testCaseId);
            Allure.label("AS_ID", testCaseId);
        }
        logTest("Start test: " + baseDisplayName);
    }

    @AfterMethod
    public void clearTestCaseContext() {
        CURRENT_TEST_CASE_ID.remove();
    }

    @BeforeClass
    public void setup() {
        configureLogger();
        RestAssured.baseURI = APIConstants.BASE_URL;
        RestAssured.filters(new AllureRestAssured());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}


