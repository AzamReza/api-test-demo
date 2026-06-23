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
 * Photos module test suite.
 */
@Feature("Photos Module")
@Story("Read and boundary checks for /photos")
public class PhotosAPITest extends BaseTest {

    @Test
    @AllureId("TC-PHT-001")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of all photos")
    public void testGetAllPhotos() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.PHOTOS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0))
            .body("[0]", hasKey("albumId"))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("title"))
            .body("[0]", hasKey("url"))
            .body("[0]", hasKey("thumbnailUrl"));
        logTest("Test Get All Photos - PASSED");
    }

    @Test
    @AllureId("TC-PHT-002")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of a specific photo by ID")
    public void testGetSpecificPhoto() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.PHOTOS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", equalTo(1))
            .body("albumId", equalTo(1))
            .body("title", notNullValue())
            .body("url", notNullValue())
            .body("thumbnailUrl", notNullValue());
        logTest("Test Get Specific Photo - PASSED");
    }

    @Test
    @AllureId("TC-PHT-003")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify filtering photos by albumId")
    public void testGetPhotosByAlbumId() {
        given()
            .queryParam("albumId", 1)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.PHOTOS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0))
            .body("[0].albumId", equalTo(1));
        logTest("Test Get Photos By Album ID - PASSED");
    }

    @Test
    @AllureId("TC-PHT-004")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify non-existing photo returns 404")
    public void testGetNonExistingPhotoReturns404() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.PHOTOS_ENDPOINT + "/5001")
        .then()
            .statusCode(APIConstants.NOT_FOUND_STATUS);
        logTest("Non-existing photo returns 404 as expected");
    }

    @Test
    @AllureId("TC-PHT-005")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify photos filter returns empty list for unknown albumId")
    public void testFilterPhotosByUnknownAlbumReturnsEmptyList() {
        given()
            .queryParam("albumId", 9999)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.PHOTOS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .body("size()", equalTo(0));
        logTest("Unknown albumId photos filter returns empty list");
    }
}

