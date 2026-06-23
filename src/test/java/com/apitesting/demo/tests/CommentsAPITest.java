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

/**
 * Comments module test suite.
 */
@Feature("Comments Module")
@Story("CRUD, filtering and boundary checks for /comments")
public class CommentsAPITest extends BaseTest {

    @Test
    @AllureId("TC-CMT-001")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of all comments")
    public void testGetAllComments() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.COMMENTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0))
            .body("[0]", hasKey("postId"))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("name"))
            .body("[0]", hasKey("email"))
            .body("[0]", hasKey("body"));
        logTest("Test Get All Comments - PASSED");
    }

    @Test
    @AllureId("TC-CMT-002")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of a specific comment by ID")
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
            .body("email", notNullValue())
            .body("body", notNullValue());
        logTest("Test Get Specific Comment - PASSED");
    }

    @Test
    @AllureId("TC-CMT-003")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify creating a comment")
    public void testCreateComment() {
        String requestBody = "{\n" +
                "  \"postId\": 1,\n" +
                "  \"name\": \"Module Wise Comment\",\n" +
                "  \"email\": \"module@example.com\",\n" +
                "  \"body\": \"Created from CommentsAPITest\"\n" +
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
            .body("name", equalTo("Module Wise Comment"))
            .body("email", equalTo("module@example.com"));
        logTest("Test Create Comment - PASSED");
    }

    @Test
    @AllureId("TC-CMT-004")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify updating a comment")
    public void testUpdateComment() {
        String requestBody = "{\n" +
                "  \"id\": 1,\n" +
                "  \"postId\": 1,\n" +
                "  \"name\": \"Updated Comment\",\n" +
                "  \"email\": \"updated@example.com\",\n" +
                "  \"body\": \"Updated from CommentsAPITest\"\n" +
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

    @Test
    @AllureId("TC-CMT-005")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify deleting a comment")
    public void testDeleteComment() {
        given()
        .when()
            .delete(APIConstants.COMMENTS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS);
        logTest("Test Delete Comment - PASSED");
    }

    @Test
    @AllureId("TC-CMT-006")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify filtering comments by postId")
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

    @Test
    @AllureId("TC-CMT-007")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify non-existing comment returns 404")
    public void testGetNonExistingCommentReturns404() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.COMMENTS_ENDPOINT + "/501")
        .then()
            .statusCode(APIConstants.NOT_FOUND_STATUS);
        logTest("Non-existing comment returns 404 as expected");
    }

    @Test
    @AllureId("TC-CMT-008")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify comments filter returns empty list for unknown postId")
    public void testFilterCommentsByUnknownPostReturnsEmptyList() {
        given()
            .queryParam("postId", 9999)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.COMMENTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .body("size()", equalTo(0));
        logTest("Unknown postId comments filter returns empty list");
    }
}

