package com.apitesting.demo.framework;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import io.qameta.allure.model.Label;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Method;

/**
 * Custom Allure listener for TestNG that removes package names
 * from suite/testClass labels in Allure reports.
 */
public class CustomAllureListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String fullClassName = result.getTestClass() == null
                ? null
                : result.getTestClass().getName();
        String simpleClassName = getSimpleClassName(fullClassName);
        String methodName = result.getMethod() == null
                ? null
                : result.getMethod().getMethodName();
        String testCaseId = resolveTestCaseId(result);
        String description = resolveDescription(result);
        String displayName = buildDisplayName(methodName, description, testCaseId);

        Allure.getLifecycle().updateTestCase(testResult -> {
            if (displayName != null && !displayName.isEmpty()) {
                testResult.setName(displayName);
            }

            if (simpleClassName != null && methodName != null) {
                testResult.setFullName(simpleClassName + "." + methodName);
            }

            if (description != null && !description.isEmpty()) {
                testResult.setDescription(description);
            }

            if (testResult.getLabels() != null) {
                boolean hasTestCaseLabel = false;
                boolean hasAsIdLabel = false;
                String featureValue = null;

                for (Label label : testResult.getLabels()) {
                    if ("feature".equals(label.getName()) && label.getValue() != null && !label.getValue().trim().isEmpty()) {
                        featureValue = label.getValue().trim();
                        break;
                    }
                }

                for (Label label : testResult.getLabels()) {
                    if ("testClass".equals(label.getName())) {
                        label.setValue(simpleClassName);
                    } else if ("suite".equals(label.getName())) {
                        label.setValue(featureValue == null ? simpleClassName : featureValue);
                    } else if ("subSuite".equals(label.getName())) {
                        label.setValue(simpleClassName);
                    } else if ("parentSuite".equals(label.getName())) {
                        label.setValue("API Test Automation");
                    } else if ("testCaseId".equals(label.getName())) {
                        hasTestCaseLabel = true;
                        label.setValue(testCaseId);
                    } else if ("AS_ID".equals(label.getName())) {
                        hasAsIdLabel = true;
                        label.setValue(testCaseId);
                    }
                }

                if (testCaseId != null && !testCaseId.isEmpty()) {
                    if (!hasTestCaseLabel) {
                        testResult.getLabels().add(new Label().setName("testCaseId").setValue(testCaseId));
                    }
                    if (!hasAsIdLabel) {
                        testResult.getLabels().add(new Label().setName("AS_ID").setValue(testCaseId));
                    }
                }
            }
        });
    }

    @Override
    public void onStart(ITestContext context) {
        // No-op
    }

    @Override
    public void onFinish(ITestContext context) {
        // No-op
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // No-op
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // No-op
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // No-op
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // No-op
    }

    private String getSimpleClassName(String fullClassName) {
        if (fullClassName == null || fullClassName.isEmpty()) {
            return fullClassName;
        }

        int lastDotIndex = fullClassName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fullClassName.length() - 1) {
            return fullClassName.substring(lastDotIndex + 1);
        }

        return fullClassName;
    }

    private String resolveTestCaseId(ITestResult result) {
        Method method = resolveJavaMethod(result);
        if (method == null) {
            return null;
        }

        AllureId allureId = method.getAnnotation(AllureId.class);
        if (allureId == null || allureId.value() == null) {
            return null;
        }

        String value = allureId.value().trim();
        return value.isEmpty() ? null : value;
    }

    private String resolveDescription(ITestResult result) {
        Method method = resolveJavaMethod(result);
        if (method == null) {
            return null;
        }

        Description annotation = method.getAnnotation(Description.class);
        if (annotation == null || annotation.value() == null) {
            return null;
        }

        String value = annotation.value().trim();
        return value.isEmpty() ? null : value;
    }

    private Method resolveJavaMethod(ITestResult result) {
        if (result == null || result.getMethod() == null || result.getTestClass() == null) {
            return null;
        }

        try {
            Class<?> clazz = result.getTestClass().getRealClass();
            String methodName = result.getMethod().getMethodName();
            return clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private String buildDisplayName(String methodName, String description, String testCaseId) {
        String baseName = description;
        if (baseName == null || baseName.isEmpty()) {
            baseName = toReadableTestName(methodName);
        }

        if (testCaseId == null || testCaseId.isEmpty()) {
            return baseName;
        }
        return testCaseId + " - " + baseName;
    }

    private String toReadableTestName(String methodName) {
        if (methodName == null || methodName.trim().isEmpty()) {
            return methodName;
        }

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
}
