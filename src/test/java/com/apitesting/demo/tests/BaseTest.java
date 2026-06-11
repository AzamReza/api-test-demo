package com.apitesting.demo.tests;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

/**
 * Base Test Class - Provides common setup for all test classes
 */
public class BaseTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}

