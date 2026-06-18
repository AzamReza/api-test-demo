package com.apitesting.demo.tests;

import com.apitesting.demo.utils.APIConstants;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private static final String TEST_CASES_DOC_RESOURCE = "TEST_CASES_DOCUMENTATION.md";
    private static final Pattern SUITE_CLASS_PATTERN = Pattern.compile("^##\\s+\\d+\\..*`([A-Za-z0-9_]+)`.*$");
    private static final Pattern TEST_CASE_ID_PATTERN = Pattern.compile("^###\\s+(TC-[A-Z]+-\\d+)\\s*:.*$");
    private static final Pattern METHOD_PATTERN = Pattern.compile("^-\\s+\\*\\*Method:\\*\\*\\s+`([A-Za-z0-9_]+)`.*$");

    private static final Map<String, String> TEST_CASE_ID_BY_CLASS_AND_METHOD = loadTestCaseIds();
    private static final ThreadLocal<String> CURRENT_TEST_CASE_ID = new ThreadLocal<>();
    private static boolean loggerConfigured = false;

    @Rule
    public TestName testName = new TestName();

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
                String currentSuiteClass = null;
                String currentTestCaseId = null;
                String line;

                while ((line = reader.readLine()) != null) {
                    Matcher suiteMatcher = SUITE_CLASS_PATTERN.matcher(line);
                    if (suiteMatcher.matches()) {
                        currentSuiteClass = suiteMatcher.group(1);
                    }

                    Matcher testCaseMatcher = TEST_CASE_ID_PATTERN.matcher(line);
                    if (testCaseMatcher.matches()) {
                        currentTestCaseId = testCaseMatcher.group(1);
                        continue;
                    }

                    if (currentTestCaseId == null || currentSuiteClass == null) {
                        continue;
                    }

                    Matcher methodMatcher = METHOD_PATTERN.matcher(line);
                    if (methodMatcher.matches()) {
                        String methodName = methodMatcher.group(1);
                        mapping.put(currentSuiteClass + "#" + methodName, currentTestCaseId);
                        currentTestCaseId = null;
                    }
                }
            }
        } catch (IOException ignored) {
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(mapping);
    }

    private static String resolveTestCaseId(String className, String methodName) {
        return TEST_CASE_ID_BY_CLASS_AND_METHOD.get(className + "#" + methodName);
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

    @Before
    public void configureAllureTestCaseMetadata() {
        String methodName = testName.getMethodName();
        if (methodName == null || methodName.trim().isEmpty()) {
            return;
        }

        String className = this.getClass().getSimpleName();
        String testCaseId = resolveTestCaseId(className, methodName);
        CURRENT_TEST_CASE_ID.set(testCaseId);

        try {
            Method testMethod = this.getClass().getMethod(methodName);
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
            }
            logTest("Start test: " + baseDisplayName);
        } catch (NoSuchMethodException ignored) {
            String readableName = toReadableTestName(methodName);
            String displayName = (testCaseId == null || testCaseId.isEmpty())
                    ? readableName
                    : testCaseId + " - " + readableName;
            Allure.getLifecycle().updateTestCase(result -> {
                result.setName(displayName);
                result.setFullName(className + "." + methodName);
            });
            if (testCaseId != null && !testCaseId.isEmpty()) {
                Allure.label("testCaseId", testCaseId);
            }
            logTest("Start test: " + readableName);
        }
    }

    @After
    public void clearTestCaseContext() {
        CURRENT_TEST_CASE_ID.remove();
    }

    @BeforeClass
    public static void setup() {
        configureLogger();
        RestAssured.baseURI = APIConstants.BASE_URL;
        RestAssured.filters(new AllureRestAssured());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}

