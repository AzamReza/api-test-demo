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
 * Albums module test suite.
 */
@Feature("Albums Module")
@Story("Read and boundary checks for /albums")
public class AlbumsAPITest extends BaseTest {

    @Test
    @AllureId("TC-ALB-001")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of all albums")
    public void testGetAllAlbums() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.ALBUMS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0))
            .body("[0]", hasKey("userId"))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("title"));
        logTest("Test Get All Albums - PASSED");
    }

    @Test
    @AllureId("TC-ALB-002")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of a specific album by ID")
    public void testGetSpecificAlbum() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.ALBUMS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", equalTo(1))
            .body("userId", equalTo(1))
            .body("title", notNullValue());
        logTest("Test Get Specific Album - PASSED");
    }

    @Test
    @AllureId("TC-ALB-003")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify filtering albums by userId")
    public void testGetAlbumsByUser() {
        given()
            .queryParam("userId", 1)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.ALBUMS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0))
            .body("[0].userId", equalTo(1));
        logTest("Test Get Albums By User - PASSED");
    }

    @Test
    @AllureId("TC-ALB-004")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify non-existing album returns 404")
    public void testGetNonExistingAlbumReturns404() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.ALBUMS_ENDPOINT + "/101")
        .then()
            .statusCode(APIConstants.NOT_FOUND_STATUS);
        logTest("Non-existing album returns 404 as expected");
    }

    @Test
    @AllureId("TC-ALB-005")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify albums filter returns empty list for unknown userId")
    public void testFilterAlbumsByUnknownUserReturnsEmptyList() {
        given()
            .queryParam("userId", 9999)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.ALBUMS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .body("size()", equalTo(0));
        logTest("Unknown userId albums filter returns empty list");
    }
}

