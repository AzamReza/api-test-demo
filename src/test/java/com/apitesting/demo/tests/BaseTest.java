package com.apitesting.demo.tests;

import com.apitesting.demo.utils.APIConstants;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        TEST_LOGGER.info(message);
        Allure.step(message);
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

        try {
            Method testMethod = this.getClass().getMethod(methodName);
            Description description = testMethod.getAnnotation(Description.class);
            String className = this.getClass().getSimpleName();
            
            if (description != null && description.value() != null && !description.value().trim().isEmpty()) {
                String testDescription = description.value().trim();
                Allure.getLifecycle().updateTestCase(result -> {
                    result.setName(testDescription);
                    result.setFullName(className + "." + methodName);
                });
                Allure.step("Start test: " + testDescription);
            } else {
                String readableName = toReadableTestName(methodName);
                Allure.getLifecycle().updateTestCase(result -> {
                    result.setName(readableName);
                    result.setFullName(className + "." + methodName);
                });
                Allure.step("Start test: " + readableName);
            }
        } catch (NoSuchMethodException ignored) {
            String readableName = toReadableTestName(methodName);
            String className = this.getClass().getSimpleName();
            Allure.getLifecycle().updateTestCase(result -> {
                result.setName(readableName);
                result.setFullName(className + "." + methodName);
            });
            Allure.step("Start test: " + readableName);
        }
    }

    @BeforeClass
    public static void setup() {
        configureLogger();
        RestAssured.baseURI = APIConstants.BASE_URL;
        RestAssured.filters(new AllureRestAssured());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}

