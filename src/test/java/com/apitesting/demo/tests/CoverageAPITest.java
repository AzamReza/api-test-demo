package com.apitesting.demo.tests;

import com.apitesting.demo.utils.APIConstants;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.apitesting.demo.tests.BaseTest.logTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Professional coverage suite focused on API contract, negative scenarios, and deterministic query behavior.
 */
@Feature("JSONPlaceholder API")
@Story("Contract, Negative and Query Coverage")
public class CoverageAPITest extends BaseTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate posts collection contract and expected cardinality")
    public void testPostsCollectionContract() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .body("size()", equalTo(100))
            .body("[0]", hasKey("userId"))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("title"))
            .body("[0]", hasKey("body"));
        logTest("Posts collection contract validated");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate comments collection contract and expected cardinality")
    public void testCommentsCollectionContract() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.COMMENTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .body("size()", equalTo(500))
            .body("[0]", hasKey("postId"))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("name"))
            .body("[0]", hasKey("email"))
            .body("[0]", hasKey("body"));
        logTest("Comments collection contract validated");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate users collection contract and expected cardinality")
    public void testUsersCollectionContract() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.USERS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .body("size()", equalTo(10))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("name"))
            .body("[0]", hasKey("username"))
            .body("[0]", hasKey("email"))
            .body("[0].address", hasKey("city"))
            .body("[0].address", hasKey("geo"))
            .body("[0].company", hasKey("name"));
        logTest("Users collection contract validated");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate posts API returns 404 for non-existing resource")
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
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate comments API returns 404 for non-existing resource")
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
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate users API returns 404 for non-existing resource")
    public void testGetNonExistingUserReturns404() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.USERS_ENDPOINT + "/11")
        .then()
            .statusCode(APIConstants.NOT_FOUND_STATUS);
        logTest("Non-existing user returns 404 as expected");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate post filtering by unknown userId returns empty result")
    public void testFilterPostsByUnknownUserReturnsEmptyList() {
        given()
            .queryParam("userId", 9999)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .body("size()", equalTo(0));
        logTest("Unknown userId post filter returns empty list");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate comments filtering by unknown postId returns empty result")
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

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate users filtering by known username returns deterministic record")
    public void testFilterUsersByKnownUsername() {
        given()
            .queryParam("username", "Bret")
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.USERS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .body("size()", equalTo(1))
            .body("[0].id", equalTo(1))
            .body("[0].username", equalTo("Bret"));
        logTest("Known username filter returns expected user");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate user email values are unique and syntactically valid")
    public void testUsersContainUniqueValidEmails() {
        Response response = given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.USERS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .extract()
            .response();

        List<String> emails = response.jsonPath().getList("email");
        Set<String> uniqueEmails = new HashSet<>(emails);

        assertEquals("User email addresses should be unique", emails.size(), uniqueEmails.size());
        assertTrue("Each email should include @", emails.stream().allMatch(email -> email != null && email.contains("@")));
        logTest("Unique and valid email format verified for users");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate _limit query parameter constrains number of returned posts")
    public void testPostsLimitQueryParameter() {
        given()
            .queryParam("_limit", 5)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .body("size()", equalTo(5));
        logTest("_limit query parameter validated for posts endpoint");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate sorting posts by id descending returns deterministic top records")
    public void testPostsSortByIdDescending() {
        Response response = given()
            .queryParam("_sort", "id")
            .queryParam("_order", "desc")
            .queryParam("_limit", 3)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .extract()
            .response();

        List<Integer> ids = response.jsonPath().getList("id");
        assertEquals("Top sorted IDs should match expected descending order", Arrays.asList(100, 99, 98), ids);
        logTest("Descending sort order validated for posts endpoint");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate PATCH operation partially updates a post")
    public void testPatchPostPartialUpdate() {
        String patchedTitle = "Patched title from automation";
        String requestBody = "{\n" +
                "  \"title\": \"" + patchedTitle + "\"\n" +
                "}";

        given()
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body(requestBody)
        .when()
            .patch(APIConstants.POSTS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .body("id", equalTo(1))
            .body("userId", equalTo(1))
            .body("title", equalTo(patchedTitle));
        logTest("PATCH partial update validated for posts endpoint");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate core caching and rate-limit headers are returned")
    public void testResponseHeadersContainOperationalMetadata() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.POSTS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .header("Cache-Control", not(isEmptyOrNullString()))
            .header("ETag", not(isEmptyOrNullString()))
            .header("X-Ratelimit-Limit", not(isEmptyOrNullString()));
        logTest("Operational response headers validated");
    }
}


