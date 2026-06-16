package com.apitesting.demo.tests;

import com.apitesting.demo.utils.APIConstants;
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
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
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
import static com.apitesting.demo.tests.BaseTest.logTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Network Mocking Test Suite - Tests API interactions with mocked HTTP responses
 * Uses WireMock to mock external API endpoints and verify application behavior
 */
@Feature("Network Mocking")
@Story("HTTP Response Mocking and Validation")
public class NetworkMockingTest extends BaseTest {

    private WireMockServer wireMockServer;

    @Before
    public void setupWireMock() {
        // Initialize WireMock server using configured mock port
        wireMockServer = new WireMockServer(APIConstants.MOCK_PORT);
        wireMockServer.start();
        WireMock.configureFor(APIConstants.MOCK_HOST, APIConstants.MOCK_PORT);

        // Set RestAssured base URI to point to mock server
        RestAssured.baseURI = APIConstants.MOCK_BASE_URL;
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
        stubFor(get(urlEqualTo(APIConstants.MOCK_USERS_ENDPOINT + "/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APIConstants.CONTENT_TYPE_JSON)
                        .withBody("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"name\": \"John Doe\",\n" +
                                "  \"email\": \"john@example.com\",\n" +
                                "  \"phone\": \"555-1234\"\n" +
                                "}")));

        // Execute test against mocked endpoint
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.MOCK_USERS_ENDPOINT + "/1")
        .then()
            .statusCode(200)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", equalTo(1))
            .body("name", equalTo("John Doe"))
            .body("email", equalTo("john@example.com"))
            .body("phone", equalTo("555-1234"));

        logTest("Test Mocked Get User Data - PASSED");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("[Network Mocking] Verify mocked GET request for posts list returns array response")
    public void testMockedGetPostsList() {
        // Mock the GET endpoint for posts list
        stubFor(get(urlEqualTo(APIConstants.MOCK_POSTS_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APIConstants.CONTENT_TYPE_JSON)
                        .withBody("[\n" +
                                "  {\"id\": 1, \"userId\": 1, \"title\": \"First Post\", \"body\": \"Content of first post\"},\n" +
                                "  {\"id\": 2, \"userId\": 1, \"title\": \"Second Post\", \"body\": \"Content of second post\"}\n" +
                                "]")));

        // Execute test against mocked endpoint
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.MOCK_POSTS_ENDPOINT)
        .then()
            .statusCode(200)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", equalTo(2))
            .body("[0].id", equalTo(1))
            .body("[0].title", equalTo("First Post"))
            .body("[1].id", equalTo(2))
            .body("[1].title", equalTo("Second Post"));

        logTest("Test Mocked Get Posts List - PASSED");
    }

    // ==================== Mocked POST Tests ====================

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("[Network Mocking] Verify mocked POST request for creating new user")
    public void testMockedCreateUser() {
        // Mock the POST endpoint for creating user
        stubFor(post(urlEqualTo(APIConstants.MOCK_USERS_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", APIConstants.CONTENT_TYPE_JSON)
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
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body(requestBody)
        .when()
            .post(APIConstants.MOCK_USERS_ENDPOINT)
        .then()
            .statusCode(201)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", equalTo(101))
            .body("name", equalTo("Jane Smith"))
            .body("email", equalTo("jane@example.com"));

        logTest("Test Mocked Create User - PASSED");
    }

    // ==================== Mocked PUT Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mocked PUT request for updating user data")
    public void testMockedUpdateUser() {
        // Mock the PUT endpoint for updating user
        stubFor(put(urlEqualTo(APIConstants.MOCK_USERS_ENDPOINT + "/1"))
                .withHeader("Content-Type", containing(APIConstants.CONTENT_TYPE_JSON))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APIConstants.CONTENT_TYPE_JSON)
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
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body(requestBody)
        .when()
            .put(APIConstants.MOCK_USERS_ENDPOINT + "/1")
        .then()
            .statusCode(200)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", equalTo(1))
            .body("name", equalTo("John Updated"))
            .body("email", equalTo("john.updated@example.com"));

        logTest("Test Mocked Update User - PASSED");
    }

    // ==================== Mocked DELETE Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mocked DELETE request for removing user")
    public void testMockedDeleteUser() {
        // Mock the DELETE endpoint
        stubFor(delete(urlEqualTo(APIConstants.MOCK_USERS_ENDPOINT + "/1"))
                .willReturn(aResponse()
                        .withStatus(204)));

        // Execute test against mocked endpoint
        given()
        .when()
            .delete(APIConstants.MOCK_USERS_ENDPOINT + "/1")
        .then()
            .statusCode(204);

        logTest("Test Mocked Delete User - PASSED");
    }

    // ==================== Mock Server Verification Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mock server received expected request")
    public void testVerifyMockServerRequest() {
        // Mock the GET endpoint
        stubFor(get(urlEqualTo(APIConstants.MOCK_USERS_ENDPOINT + "/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APIConstants.CONTENT_TYPE_JSON)
                        .withBody("{\"id\": 1, \"name\": \"Test User\"}")));

        // Make the request
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.MOCK_USERS_ENDPOINT + "/1")
        .then()
            .statusCode(200);

        // Verify that the mock server received exactly one request to the endpoint
        verify(exactly(1), getRequestedFor(urlEqualTo(APIConstants.MOCK_USERS_ENDPOINT + "/1")));

        logTest("Test Verify Mock Server Request - PASSED");
    }

    // ==================== Error Response Mocking Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mocked error response for not found scenario")
    public void testMockedErrorResponse404() {
        // Mock the GET endpoint to return 404
        stubFor(get(urlEqualTo(APIConstants.MOCK_USERS_ENDPOINT + "/999"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", APIConstants.CONTENT_TYPE_JSON)
                        .withBody("{\"error\": \"User not found\"}")));

        // Execute test against mocked endpoint
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.MOCK_USERS_ENDPOINT + "/999")
        .then()
            .statusCode(404)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("error", equalTo("User not found"));

        logTest("Test Mocked Error Response 404 - PASSED");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mocked server error response")
    public void testMockedErrorResponse500() {
        // Mock the GET endpoint to return 500
        stubFor(post(urlEqualTo(APIConstants.MOCK_USERS_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", APIConstants.CONTENT_TYPE_JSON)
                        .withBody("{\"error\": \"Internal Server Error\"}")));

        String requestBody = "{\"name\": \"Test\"}";

        // Execute test against mocked endpoint
        given()
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body(requestBody)
        .when()
            .post(APIConstants.MOCK_USERS_ENDPOINT)
        .then()
            .statusCode(500)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("error", equalTo("Internal Server Error"));

        logTest("Test Mocked Error Response 500 - PASSED");
    }

    // ==================== Query Parameter Mocking Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mocked GET request with query parameters")
    public void testMockedGetWithQueryParameters() {
        // Mock the GET endpoint with query parameters
        stubFor(get(urlPathEqualTo(APIConstants.MOCK_POSTS_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APIConstants.CONTENT_TYPE_JSON)
                        .withBody("[\n" +
                                "  {\"id\": 1, \"userId\": 1, \"title\": \"User 1 Post\"}\n" +
                                "]")));

        // Execute test against mocked endpoint
        given()
            .queryParam("userId", "1")
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.MOCK_POSTS_ENDPOINT)
        .then()
            .statusCode(200)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", equalTo(1))
            .body("[0].userId", equalTo(1));

        logTest("Test Mocked Get With Query Parameters - PASSED");
    }

    // ==================== Multiple Requests Mocking Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[Network Mocking] Verify mocking multiple sequential API requests")
    public void testMockedMultipleSequentialRequests() {
        // Mock first endpoint
        stubFor(post(urlEqualTo(APIConstants.MOCK_USERS_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", APIConstants.CONTENT_TYPE_JSON)
                        .withBody("{\"id\": 101, \"name\": \"New User\"}")));

        // Mock second endpoint
        stubFor(get(urlEqualTo(APIConstants.MOCK_USERS_ENDPOINT + "/101"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APIConstants.CONTENT_TYPE_JSON)
                        .withBody("{\"id\": 101, \"name\": \"New User\", \"email\": \"newuser@example.com\"}")));

        // First request - Create user
        given()
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("{\"name\": \"New User\"}")
        .when()
            .post(APIConstants.MOCK_USERS_ENDPOINT)
        .then()
            .statusCode(201)
            .body("id", equalTo(101));

        // Second request - Get the created user
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.MOCK_USERS_ENDPOINT + "/101")
        .then()
            .statusCode(200)
            .body("id", equalTo(101))
            .body("email", equalTo("newuser@example.com"));

        logTest("Test Mocked Multiple Sequential Requests - PASSED");
    }
}

