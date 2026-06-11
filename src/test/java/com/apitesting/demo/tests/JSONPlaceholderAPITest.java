package com.apitesting.demo.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * API Test Suite for JSONPlaceholder Public API
 * Tests CRUD operations on Posts, Comments, and Users endpoints
 */
@Feature("JSONPlaceholder API")
@Story("CRUD Operations and Filtering")
public class JSONPlaceholderAPITest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    // ==================== GET Tests ====================

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of all posts from API")
    public void testGetAllPosts() {
        given()
            .accept("application/json")
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("size()", greaterThan(0))
            .body("[0]", hasKey("userId"))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("title"))
            .body("[0]", hasKey("body"));
        System.out.println("✁ETest Get All Posts - PASSED");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of a specific post by ID")
    public void testGetSpecificPost() {
        given()
            .accept("application/json")
        .when()
            .get("/posts/1")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("id", equalTo(1))
            .body("userId", equalTo(1))
            .body("title", notNullValue())
            .body("body", notNullValue());
        System.out.println("✁ETest Get Specific Post - PASSED");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify retrieval of comments for a specific post")
    public void testGetPostComments() {
        given()
            .accept("application/json")
        .when()
            .get("/posts/1/comments")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("size()", greaterThan(0))
            .body("[0]", hasKey("postId"))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("name"))
            .body("[0]", hasKey("email"))
            .body("[0]", hasKey("body"));
        System.out.println("✁ETest Get Post Comments - PASSED");
    }

    @Test
    public void testGetAllComments() {
        given()
            .accept("application/json")
        .when()
            .get("/comments")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("size()", greaterThan(0));
        System.out.println("✁ETest Get All Comments - PASSED");
    }

    @Test
    public void testGetSpecificComment() {
        given()
            .accept("application/json")
        .when()
            .get("/comments/1")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("id", equalTo(1))
            .body("postId", notNullValue())
            .body("name", notNullValue())
            .body("email", notNullValue());
        System.out.println("✁ETest Get Specific Comment - PASSED");
    }

    @Test
    public void testGetAllUsers() {
        given()
            .accept("application/json")
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("size()", greaterThan(0))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("name"))
            .body("[0]", hasKey("email"))
            .body("[0]", hasKey("address"))
            .body("[0]", hasKey("phone"))
            .body("[0]", hasKey("website"))
            .body("[0]", hasKey("company"));
        System.out.println("✁ETest Get All Users - PASSED");
    }

    @Test
    public void testGetSpecificUser() {
        given()
            .accept("application/json")
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("id", equalTo(1))
            .body("name", notNullValue())
            .body("email", notNullValue());
        System.out.println("✁ETest Get Specific User - PASSED");
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
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .contentType("application/json")
            .body("id", notNullValue())
            .body("title", equalTo("Test Post"))
            .body("body", equalTo("This is a test post created for automation testing"))
            .body("userId", equalTo(1));
        System.out.println("✁ETest Create Post - PASSED");
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
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/comments")
        .then()
            .statusCode(201)
            .contentType("application/json")
            .body("id", notNullValue())
            .body("postId", equalTo(1))
            .body("name", equalTo("Test Comment"))
            .body("email", equalTo("test@example.com"));
        System.out.println("✁ETest Create Comment - PASSED");
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
            .contentType("application/json")
            .body(requestBody)
        .when()
            .put("/posts/1")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("id", equalTo(1))
            .body("title", equalTo("Updated Post Title"))
            .body("body", equalTo("This post has been updated"));
        System.out.println("✁ETest Update Post - PASSED");
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
            .contentType("application/json")
            .body(requestBody)
        .when()
            .put("/comments/1")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("id", equalTo(1))
            .body("name", equalTo("Updated Comment"));
        System.out.println("✁ETest Update Comment - PASSED");
    }

    // ==================== DELETE Tests ====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify deletion of a post via DELETE request")
    public void testDeletePost() {
        given()
        .when()
            .delete("/posts/1")
        .then()
            .statusCode(200);
        System.out.println("✁ETest Delete Post - PASSED");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify deletion of a comment via DELETE request")
    public void testDeleteComment() {
        given()
        .when()
            .delete("/comments/1")
        .then()
            .statusCode(200);
        System.out.println("✁ETest Delete Comment - PASSED");
    }

    // ==================== Query Parameter Tests ====================

    @Test
    public void testGetPostsByUser() {
        given()
            .queryParam("userId", 1)
            .accept("application/json")
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("size()", greaterThan(0))
            .body("[0].userId", equalTo(1));
        System.out.println("✁ETest Get Posts By User - PASSED");
    }

    @Test
    public void testGetCommentsByPostId() {
        given()
            .queryParam("postId", 1)
            .accept("application/json")
        .when()
            .get("/comments")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("size()", greaterThan(0))
            .body("[0].postId", equalTo(1));
        System.out.println("✁ETest Get Comments By Post ID - PASSED");
    }

    // ==================== Response Time Tests ====================

    @Test
    public void testResponseTimeForGetAllPosts() {
        given()
            .accept("application/json")
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .time(lessThan(5000L)); // Response should be less than 5 seconds
        System.out.println("✁ETest Response Time for Get All Posts - PASSED");
    }
}

