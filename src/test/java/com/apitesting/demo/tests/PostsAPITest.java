package com.apitesting.demo.tests;

import com.apitesting.demo.framework.BaseTest;
import com.apitesting.demo.utils.APIConstants;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static com.apitesting.demo.framework.BaseTest.logTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.lessThan;

/**
 * Posts module test suite.
 */
@Feature("Posts Module")
@Story("CRUD, filtering and boundary checks for /posts")
public class PostsAPITest extends BaseTest {

    @Test
    @AllureId("TC-PST-001")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of all posts")
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
    @AllureId("TC-PST-002")
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
    @AllureId("TC-PST-003")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify creating a post")
    public void testCreatePost() {
        String requestBody = "{\n" +
                "  \"title\": \"Module Wise Post\",\n" +
                "  \"body\": \"Created from PostsAPITest\",\n" +
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
            .body("title", equalTo("Module Wise Post"))
            .body("body", equalTo("Created from PostsAPITest"))
            .body("userId", equalTo(1));
        logTest("Test Create Post - PASSED");
    }

    @Test
    @AllureId("TC-PST-004")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify updating a post")
    public void testUpdatePost() {
        String requestBody = "{\n" +
                "  \"id\": 1,\n" +
                "  \"title\": \"Updated Post Title\",\n" +
                "  \"body\": \"Updated from PostsAPITest\",\n" +
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
            .body("body", equalTo("Updated from PostsAPITest"));
        logTest("Test Update Post - PASSED");
    }

    @Test
    @AllureId("TC-PST-005")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify deleting a post")
    public void testDeletePost() {
        given()
        .when()
            .delete(APIConstants.POSTS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS);
        logTest("Test Delete Post - PASSED");
    }

    @Test
    @AllureId("TC-PST-006")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify filtering posts by userId")
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
    @AllureId("TC-PST-007")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify non-existing post returns 404")
    public void testGetNonExistingPostReturns404() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT + "/101")
        .then()
            .statusCode(APIConstants.NOT_FOUND_STATUS);
        logTest("Non-existing post returns 404 as expected");
    }

    @Test
    @AllureId("TC-PST-008")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify posts limit and descending sort return deterministic top records")
    public void testPostsLimitAndSort() {
        given()
            .queryParam("_sort", "id")
            .queryParam("_order", "desc")
            .queryParam("_limit", 3)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", equalTo(3))
            .body("[0].id", equalTo(100))
            .body("[1].id", equalTo(99))
            .body("[2].id", equalTo(98));
        logTest("Descending sort order validated for posts endpoint");
    }

    @Test
    @AllureId("TC-PST-009")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify posts response time stays below threshold")
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

