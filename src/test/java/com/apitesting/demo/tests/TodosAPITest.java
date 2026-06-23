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
 * Todos module test suite.
 */
@Feature("Todos Module")
@Story("Read and boundary checks for /todos")
public class TodosAPITest extends BaseTest {

    @Test
    @AllureId("TC-TDO-001")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of all todos")
    public void testGetAllTodos() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.TODOS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0))
            .body("[0]", hasKey("userId"))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("title"))
            .body("[0]", hasKey("completed"));
        logTest("Test Get All Todos - PASSED");
    }

    @Test
    @AllureId("TC-TDO-002")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify retrieval of a specific todo by ID")
    public void testGetSpecificTodo() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.TODOS_ENDPOINT + "/1")
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("id", equalTo(1))
            .body("userId", equalTo(1))
            .body("title", notNullValue())
            .body("completed", notNullValue());
        logTest("Test Get Specific Todo - PASSED");
    }

    @Test
    @AllureId("TC-TDO-003")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify filtering todos by completed status")
    public void testGetCompletedTodos() {
        given()
            .queryParam("completed", true)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.TODOS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .contentType(APIConstants.CONTENT_TYPE_JSON)
            .body("size()", greaterThan(0))
            .body("[0].completed", equalTo(true));
        logTest("Test Get Completed Todos - PASSED");
    }

    @Test
    @AllureId("TC-TDO-004")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify non-existing todo returns 404")
    public void testGetNonExistingTodoReturns404() {
        given()
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.TODOS_ENDPOINT + "/201")
        .then()
            .statusCode(APIConstants.NOT_FOUND_STATUS);
        logTest("Non-existing todo returns 404 as expected");
    }

    @Test
    @AllureId("TC-TDO-005")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify todos filter returns empty list for unknown userId")
    public void testFilterTodosByUnknownUserReturnsEmptyList() {
        given()
            .queryParam("userId", 9999)
            .accept(APIConstants.CONTENT_TYPE_JSON)
        .when()
            .get(APIConstants.TODOS_ENDPOINT)
        .then()
            .statusCode(APIConstants.OK_STATUS)
            .body("size()", equalTo(0));
        logTest("Unknown userId todos filter returns empty list");
    }
}

