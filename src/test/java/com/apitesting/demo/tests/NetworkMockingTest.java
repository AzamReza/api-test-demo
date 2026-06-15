package com.apitesting.demo.tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Network Mocking Test Suite - Tests API interactions with mocked HTTP responses
 * Uses WireMock to mock external API endpoints and verify application behavior
 */
@Feature("Network Mocking")
@Story("HTTP Response Mocking and Validation")
public class NetworkMockingTest {

    private WireMockServer wireMockServer;

    @Before
    public void setup() {
        // Initialize WireMock server on port 8080
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);
        
        // Set RestAssured base URI to point to mock server
        RestAssured.baseURI = "http://localhost:8080";
    }

    @After
    public void teardown() {
        // Stop WireMock server after tests
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    // ==================== Mocked GET Tests ====================

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("[Network Mocking] Verify mocked GET request for user data returns correct response")
    public void testMockedGetUserData() {
        // Mock the GET endpoint for user data
        stubFor(get(urlEqualTo("/api/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"name\": \"John Doe\",\n" +
                                "  \"email\": \"john@example.com\",\n" +
                                "  \"phone\": \"555-1234\"\n" +
                                "}")));

        // Execute test against mocked endpoint
        given()
            .accept("application/json")
        .when()
            .get("/api/users/1")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("id", equalTo(1))
            .body("name", equalTo("John Doe"))
            .body("email", equalTo("john@example.com"))
            .body("phone", equalTo("555-1234"));
        
        System.out.println("Test Mocked Get User Data - PASSED");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("[Network Mocking] Verify mocked GET request for posts list returns array response")
    public void testMockedGetPostsList() {
        // Mock the GET endpoint for posts list
        stubFor(get(urlEqualTo("/api/posts"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[\n" +
                                "  {\"id\": 1, \"userId\": 1, \"title\": \"First Post\", \"body\": \"Content of first post\"},\n" +
                                "  {\"id\": 2, \"userId\": 1, \"title\": \"Second Post\", \"body\": \"Content of second post\"}\n" +
                                "]")));

        // Execute test against mocked endpoint
        given()
            .accept("application/json")
        .when()
            .get("/api/posts")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("size()", equalTo(2))
            .body("[0].id", equalTo(1))
            .body("[0].title", equalTo("First Post"))
            .body("[1].id", equalTo(2))
            .body("[1].title", equalTo("Second Post"));
        
        System.out.println("Test Mocked Get Posts List - PASSED");
    }

    // ==================== Mocked POST Tests ====================

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("[Network Mocking] Verify mocked POST request for creating new user")
    public void testMockedCreateUser() {
        // Mock the POST endpoint for creating user
        stubFor(post(urlEqualTo("/api/users"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "  \"id\": 101,\n" +
                                "  \"name\": \"Jane Smith\",\n" +
                                "  \"email\": \"jane@example.com\",\n" +
                                "  \"phone\": \"555-5678\"\n" +
                                "}")));

        String requestBody = "{\n" +
                "  \"name\": \"Jane Smith\",\n" +
                "  \"email\": \"jane@example.com\",\n" +
                "  \"phone\": \"555-5678\"\n" +
                "}";

        // Execute test against mocked endpoint
        given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/api/users")
        .then()
            .statusCode(201)
            .contentType("application/json")
            .body("id", equalTo(101))
            .body("name", equalTo("Jane Smith"))
            .body("email", equalTo("jane@example.com"));
        
        System.out.println("Test Mocked Create User - PASSED");
    }

    // ==================== Mocked PUT Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mocked PUT request for updating user data")
    public void testMockedUpdateUser() {
        // Mock the PUT endpoint for updating user
        stubFor(put(urlEqualTo("/api/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"name\": \"John Updated\",\n" +
                                "  \"email\": \"john.updated@example.com\",\n" +
                                "  \"phone\": \"555-9999\"\n" +
                                "}")));

        String requestBody = "{\n" +
                "  \"name\": \"John Updated\",\n" +
                "  \"email\": \"john.updated@example.com\",\n" +
                "  \"phone\": \"555-9999\"\n" +
                "}";

        // Execute test against mocked endpoint
        given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .put("/api/users/1")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("id", equalTo(1))
            .body("name", equalTo("John Updated"))
            .body("email", equalTo("john.updated@example.com"));
        
        System.out.println("Test Mocked Update User - PASSED");
    }

    // ==================== Mocked DELETE Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mocked DELETE request for removing user")
    public void testMockedDeleteUser() {
        // Mock the DELETE endpoint
        stubFor(delete(urlEqualTo("/api/users/1"))
                .willReturn(aResponse()
                        .withStatus(204)));

        // Execute test against mocked endpoint
        given()
        .when()
            .delete("/api/users/1")
        .then()
            .statusCode(204);
        
        System.out.println("Test Mocked Delete User - PASSED");
    }

    // ==================== Mock Server Verification Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mock server received expected request")
    public void testVerifyMockServerRequest() {
        // Mock the GET endpoint
        stubFor(get(urlEqualTo("/api/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"Test User\"}")));

        // Make the request
        given()
            .accept("application/json")
        .when()
            .get("/api/users/1")
        .then()
            .statusCode(200);

        // Verify that the mock server received exactly one request to the endpoint
        verify(exactly(1), getRequestedFor(urlEqualTo("/api/users/1")));
        
        System.out.println("Test Verify Mock Server Request - PASSED");
    }

    // ==================== Error Response Mocking Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mocked error response for not found scenario")
    public void testMockedErrorResponse404() {
        // Mock the GET endpoint to return 404
        stubFor(get(urlEqualTo("/api/users/999"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"User not found\"}")));

        // Execute test against mocked endpoint
        given()
            .accept("application/json")
        .when()
            .get("/api/users/999")
        .then()
            .statusCode(404)
            .contentType("application/json")
            .body("error", equalTo("User not found"));
        
        System.out.println("Test Mocked Error Response 404 - PASSED");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mocked server error response")
    public void testMockedErrorResponse500() {
        // Mock the GET endpoint to return 500
        stubFor(post(urlEqualTo("/api/users"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Internal Server Error\"}")));

        String requestBody = "{\"name\": \"Test\"}";

        // Execute test against mocked endpoint
        given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/api/users")
        .then()
            .statusCode(500)
            .contentType("application/json")
            .body("error", equalTo("Internal Server Error"));
        
        System.out.println("Test Mocked Error Response 500 - PASSED");
    }

    // ==================== Query Parameter Mocking Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mocked GET request with query parameters")
    public void testMockedGetWithQueryParameters() {
        // Mock the GET endpoint with query parameters
        stubFor(get(urlPathEqualTo("/api/posts"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[\n" +
                                "  {\"id\": 1, \"userId\": 1, \"title\": \"User 1 Post\"}\n" +
                                "]")));

        // Execute test against mocked endpoint
        given()
            .queryParam("userId", "1")
            .accept("application/json")
        .when()
            .get("/api/posts")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("size()", equalTo(1))
            .body("[0].userId", equalTo(1));
        
        System.out.println("Test Mocked Get With Query Parameters - PASSED");
    }

    // ==================== Multiple Requests Mocking Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mocking multiple sequential API requests")
    public void testMockedMultipleSequentialRequests() {
        // Mock first endpoint
        stubFor(post(urlEqualTo("/api/users"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 101, \"name\": \"New User\"}")));

        // Mock second endpoint
        stubFor(get(urlEqualTo("/api/users/101"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 101, \"name\": \"New User\", \"email\": \"newuser@example.com\"}")));

        // First request - Create user
        given()
            .contentType("application/json")
            .body("{\"name\": \"New User\"}")
        .when()
            .post("/api/users")
        .then()
            .statusCode(201)
            .body("id", equalTo(101));

        // Second request - Get the created user
        given()
            .accept("application/json")
        .when()
            .get("/api/users/101")
        .then()
            .statusCode(200)
            .body("id", equalTo(101))
            .body("email", equalTo("newuser@example.com"));
        
        System.out.println("Test Mocked Multiple Sequential Requests - PASSED");
    }
}







