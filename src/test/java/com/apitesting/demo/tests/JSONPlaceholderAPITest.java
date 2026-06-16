package com.apitesting.demo.tests;

import com.apitesting.demo.utils.APIConstants;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import static com.apitesting.demo.tests.BaseTest.logTest;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * API Test Suite for JSONPlaceholder Public API
 * Tests CRUD operations on Posts, Comments, and Users endpoints
 */
@Feature("JSONPlaceholder API")
@Story("CRUD Operations and Filtering")
public class JSONPlaceholderAPITest extends BaseTest {


    // ==================== GET Tests ====================

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of all posts from API")
    public void testGetAllPosts() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0))
            .body("[0]", hasKey("userId"))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("title"))
            .body("[0]", hasKey("body"));
        logTest("Test Get All Posts - PASSED");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of a specific post by ID")
    public void testGetSpecificPost() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", equalTo(1))
            .body("userId", equalTo(1))
            .body("title", notNullValue())
            .body("body", notNullValue());
        logTest("Test Get Specific Post - PASSED");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify retrieval of comments for a specific post")
    public void testGetPostComments() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT + "/1" + APIConstants.COMMENTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0))
            .body("[0]", hasKey("postId"))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("name"))
            .body("[0]", hasKey("email"))
            .body("[0]", hasKey("body"));
        logTest("Test Get Post Comments - PASSED");
    }

    @Test
    public void testGetAllComments() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.COMMENTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0));
        logTest("Test Get All Comments - PASSED");
    }

    @Test
    public void testGetSpecificComment() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.COMMENTS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", equalTo(1))
            .body("postId", notNullValue())
            .body("name", notNullValue())
            .body("email", notNullValue());
        logTest("Test Get Specific Comment - PASSED");
    }

    @Test
    public void testGetAllUsers() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.USERS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("name"))
            .body("[0]", hasKey("email"))
            .body("[0]", hasKey("address"))
            .body("[0]", hasKey("phone"))
            .body("[0]", hasKey("website"))
            .body("[0]", hasKey("company"));
        logTest("Test Get All Users - PASSED");
    }

    @Test
    public void testGetSpecificUser() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.USERS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", equalTo(1))
            .body("name", notNullValue())
            .body("email", notNullValue());
        logTest("Test Get Specific User - PASSED");
    }

    // ==================== POST Tests ====================

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify creation of a new post via POST request")
    public void testCreatePost() {
        String requestBody = "{\n" +
                "  \"title\": \"Test Post\",\n" +
                "  \"body\": \"This is a test post created for automation testing\",\n" +
                "  \"userId\": 1\n" +
                "}";

        given()
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body(requestBody)
        .when()
            .post(APIConstants.POSTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.CREATED_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", notNullValue())
            .body("title", equalTo("Test Post"))
            .body("body", equalTo("This is a test post created for automation testing"))
            .body("userId", equalTo(1));
        logTest("Test Create Post - PASSED");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify creation of a new comment via POST request")
    public void testCreateComment() {
        String requestBody = "{\n" +
                "  \"postId\": 1,\n" +
                "  \"name\": \"Test Comment\",\n" +
                "  \"email\": \"test@example.com\",\n" +
                "  \"body\": \"This is a test comment\"\n" +
                "}";

        given()
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body(requestBody)
        .when()
            .post(APIConstants.COMMENTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.CREATED_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", notNullValue())
            .body("postId", equalTo(1))
            .body("name", equalTo("Test Comment"))
            .body("email", equalTo("test@example.com"));
        logTest("Test Create Comment - PASSED");
    }

    // ==================== PUT Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify updating an existing post via PUT request")
    public void testUpdatePost() {
        String requestBody = "{\n" +
                "  \"id\": 1,\n" +
                "  \"title\": \"Updated Post Title\",\n" +
                "  \"body\": \"This post has been updated\",\n" +
                "  \"userId\": 1\n" +
                "}";

        given()
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body(requestBody)
        .when()
            .put(APIConstants.POSTS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", equalTo(1))
            .body("title", equalTo("Updated Post Title"))
            .body("body", equalTo("This post has been updated"));
        logTest("Test Update Post - PASSED");
    }

    @Test
    public void testUpdateComment() {
        String requestBody = "{\n" +
                "  \"id\": 1,\n" +
                "  \"postId\": 1,\n" +
                "  \"name\": \"Updated Comment\",\n" +
                "  \"email\": \"updated@example.com\",\n" +
                "  \"body\": \"This comment has been updated\"\n" +
                "}";

        given()
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body(requestBody)
        .when()
            .put(APIConstants.COMMENTS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", equalTo(1))
            .body("name", equalTo("Updated Comment"));
        logTest("Test Update Comment - PASSED");
    }

    // ==================== DELETE Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify deletion of a post via DELETE request")
    public void testDeletePost() {
        given()
        .when()
            .delete(APIConstants.POSTS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS);
        logTest("Test Delete Post - PASSED");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify deletion of a comment via DELETE request")
    public void testDeleteComment() {
        given()
        .when()
            .delete(APIConstants.COMMENTS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS);
        logTest("Test Delete Comment - PASSED");
    }

    // ==================== Query Parameter Tests ====================

    @Test
    public void testGetPostsByUser() {
        given()
            .queryParam("userId", 1)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0))
            .body("[0].userId", equalTo(1));
        logTest("Test Get Posts By User - PASSED");
    }

    @Test
    public void testGetCommentsByPostId() {
        given()
            .queryParam("postId", 1)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.COMMENTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0))
            .body("[0].postId", equalTo(1));
        logTest("Test Get Comments By Post ID - PASSED");
    }

    // ==================== Response Time Tests ====================

    @Test
    public void testResponseTimeForGetAllPosts() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .time(lessThan(APIConstants.MAX_RESPONSE_TIME));
        logTest("Test Response Time for Get All Posts - PASSED");
    }
}

