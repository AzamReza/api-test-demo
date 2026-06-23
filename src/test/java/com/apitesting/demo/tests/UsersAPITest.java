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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Users module test suite.
 */
@Feature("Users Module")
@Story("Read and boundary checks for /users")
public class UsersAPITest extends BaseTest {

    @Test
    @AllureId("TC-USR-001")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of all users")
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
    @AllureId("TC-USR-002")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of a specific user by ID")
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

    @Test
    @AllureId("TC-USR-003")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify non-existing user returns 404")
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
    @AllureId("TC-USR-004")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify filtering users by username returns a deterministic record")
    public void testFilterUsersByKnownUsername() {
        given()
            .queryParam("username", "Bret")
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.USERS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", equalTo(1))
            .body("[0].id", equalTo(1))
            .body("[0].username", equalTo("Bret"));
        logTest("Known username filter returns expected user");
    }

    @Test
    @AllureId("TC-USR-005")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify user email values are unique and syntactically valid")
    public void testUsersContainUniqueValidEmails() {
        List<String> emails = given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.USERS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .extract()
            .jsonPath()
            .getList("email");

        Set<String> uniqueEmails = new HashSet<>(emails);
        assertEquals(emails.size(), uniqueEmails.size(), "User email addresses should be unique");
        assertTrue(emails.stream().allMatch(email -> email != null && email.contains("@")),
                "Each email should include @");
        logTest("Unique and valid email format verified for users");
    }
}

