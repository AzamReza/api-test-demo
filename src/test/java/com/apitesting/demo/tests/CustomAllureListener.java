package com.apitesting.demo.tests;

import io.qameta.allure.Allure;
import io.qameta.allure.junit4.AllureJunit4;
import io.qameta.allure.model.Label;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import java.util.List;

/**
 * Custom Allure Listener that removes package names from test class names in reports
 * Displays only the simple class name (e.g., AdvancedAPITest instead of com.apitesting.demo.tests.AdvancedAPITest)
 */
public class CustomAllureListener extends RunListener {

    private final AllureJunit4 delegate = new AllureJunit4();

    @Override
    public void testStarted(Description description) {
        // Delegate to Allure first
        delegate.testStarted(description);
        
        // Then modify the class name in the current test result
        String fullClassName = description.getClassName();
        String simpleClassName = getSimpleClassName(fullClassName);
        String methodName = description.getMethodName();
        
        // Update the Allure lifecycle with the simple class name
        Allure.getLifecycle().updateTestCase(testResult -> {
            // Change the full name to use simple class name
            testResult.setFullName(simpleClassName + "." + methodName);
            
            // Update labels to use simple class name where appropriate
            if (testResult.getLabels() != null) {
                for (Label label : testResult.getLabels()) {
                    if ("testClass".equals(label.getName())) {
                        label.setValue(simpleClassName);
                    } else if ("suite".equals(label.getName())) {
                        label.setValue(simpleClassName);
                    }
                }
            }
        });
    }

    @Override
    public void testFinished(Description description) {
        delegate.testFinished(description);
    }

    @Override
    public void testFailure(org.junit.runner.notification.Failure failure) {
        delegate.testFailure(failure);
    }

    @Override
    public void testAssumptionFailure(org.junit.runner.notification.Failure failure) {
        delegate.testAssumptionFailure(failure);
    }

    @Override
    public void testIgnored(Description description) {
        delegate.testIgnored(description);
    }

    /**
     * Extracts the simple class name from a fully qualified class name
     * @param fullClassName the fully qualified class name
     * @return the simple class name
     */
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
}






