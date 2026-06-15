package com.apitesting.demo.tests;

import com.apitesting.demo.models.Post;
import com.apitesting.demo.models.User;
import com.apitesting.demo.utils.APIConstants;
import io.restassured.http.ContentType;
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
import static org.junit.Assert.*;

/**
 * Advanced API Test Suite - Tests with deserialization and object validation
 */
@Feature("JSONPlaceholder API")
@Story("Advanced Object Deserialization and Validation")
public class AdvancedAPITest extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify JSON to Java object deserialization for Post")
    public void testGetPostAsObject() {
        Post post = given()
            .accept(ContentType.JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT + "/1")
        .as(Post.class);

        assertNotNull("Post object should not be null", post);
        assertEquals("Post ID should be 1", 1, post.getId());
        assertEquals("User ID should be 1", 1, post.getUserId());
        assertNotNull("Post title should not be null", post.getTitle());
        assertNotNull("Post body should not be null", post.getBody());
        System.out.println("Retrieved Post as Object: " + post);
        System.out.println("Test Get Post As Object - PASSED");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify JSON to Java object deserialization for User")
    public void testGetUserAsObject() {
        User user = given()
            .accept(ContentType.JSON)
        .when()
            .get(APIConstants.USERS_ENDPOINT + "/1")
        .as(User.class);

        assertNotNull("User object should not be null", user);
        assertEquals("User ID should be 1", 1, user.getId());
        assertNotNull("User name should not be null", user.getName());
        assertNotNull("User email should not be null", user.getEmail());
        assertNotNull("User username should not be null", user.getUsername());
        System.out.println("✁ERetrieved User as Object: " + user);
        System.out.println("✁ETest Get User As Object - PASSED");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify creation of post using Java object serialization")
    public void testCreatePostUsingObject() {
        Post newPost = new Post(1, "Automated Test Post", "This post was created by automated test");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(newPost)
        .when()
            .post(APIConstants.POSTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.CREATED_STATUS)
            .extract()
            .response();

        Post createdPost = response.as(Post.class);
        assertNotNull("Created post should not be null", createdPost);
        assertEquals("Title should match", "Automated Test Post", createdPost.getTitle());
        assertEquals("Body should match", "This post was created by automated test", createdPost.getBody());
        assertEquals("User ID should match", 1, createdPost.getUserId());
        System.out.println("Created Post: " + createdPost);
        System.out.println("Test Create Post Using Object - PASSED");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify bulk retrieval and array deserialization of multiple posts")
    public void testMultiplePostsRetrieval() {
        Post[] posts = given()
            .accept(ContentType.JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .as(Post[].class);

        assertTrue("Should retrieve multiple posts", posts.length > 0);
        assertTrue("Should have at least 10 posts", posts.length >= 10);
        
        for (Post post : posts) {
            assertNotNull("Post ID should not be null", post.getId());
            assertNotNull("Post title should not be null", post.getTitle());
            assertNotNull("Post body should not be null", post.getBody());
            assertTrue("User ID should be greater than 0", post.getUserId() > 0);
        }
        System.out.println("Retrieved " + posts.length + " posts successfully");
        System.out.println("Test Multiple Posts Retrieval - PASSED");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify filtering posts by user ID with object deserialization")
    public void testFilterPostsByUserId() {
        Post[] posts = given()
            .queryParam("userId", 2)
            .accept(ContentType.JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .as(Post[].class);

        assertTrue("Should retrieve posts", posts.length > 0);
        
        for (Post post : posts) {
            assertEquals("All posts should be from user 2", 2, post.getUserId());
        }
        System.out.println("Retrieved " + posts.length + " posts for User ID 2");
        System.out.println("Test Filter Posts By User ID - PASSED");
    }

    @Test
    public void testResponseHeaders() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .header("Content-Type", containsString("application/json"))
            .header("Transfer-Encoding", notNullValue());
        System.out.println("Test Response Headers - PASSED");
    }

    @Test
    public void testResponseBodyValidation() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .body("id", equalTo(1))
            .body("userId", notNullValue())
            .body("title", isA(String.class))
            .body("body", isA(String.class));
        System.out.println("Test Response Body Validation - PASSED");
    }

    @Test
    public void testQueryParameterWithFiltering() {
        int userId = 1;
        Post[] userPosts = given()
            .queryParam("userId", userId)
            .accept(ContentType.JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .as(Post[].class);

        assertTrue("Should return posts for specified user", userPosts.length > 0);
        for (Post post : userPosts) {
            assertEquals("All posts must be from correct user", userId, post.getUserId());
        }
        System.out.println("Retrieved " + userPosts.length + " posts for User " + userId);
        System.out.println("Query Parameter With Filtering - PASSED");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify data consistency between related resources (posts and comments)")
    public void testDataConsistency() {
        // Get a post and then get its comments to verify consistency
        Post post = given()
            .accept(ContentType.JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT + "/1")
        .as(Post.class);

        // Get comments for that post
        Response response = given()
            .queryParam("postId", post.getId())
            .accept(ContentType.JSON)
        .when()
            .get(APIConstants.COMMENTS_ENDPOINT);

        int commentCount = response.body().path("size()");
        assertTrue("Post should have comments", commentCount > 0);
        System.out.println("Post 1 has " + commentCount + " comments");
        System.out.println("Test Data Consistency - PASSED");
    }
}

