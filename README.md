# API Testing Automation Demo

Single source of truth for this repository.

## Overview

This is a Java + Maven API automation project for `https://jsonplaceholder.typicode.com` with:

- REST API tests using RestAssured + TestNG
- Network mocking tests using WireMock
- Allure reporting
- Test case traceability mapped from `src/test/resources/TEST_CASES_DOCUMENTATION.csv`

## Current Test Suites

### API and Mocking Suites (default run)

- `PostsAPITest` (9 tests)
- `CommentsAPITest` (8 tests)
- `AlbumsAPITest` (5 tests)
- `PhotosAPITest` (5 tests)
- `TodosAPITest` (5 tests)
- `UsersAPITest` (5 tests)
- `NetworkMockingTest` (10 tests)

Default automated tests: **47**

### Guardrail Suite (separate profile)

- `TestCaseTraceabilityTest` (2 tests)

This suite validates:

- documented methods match implemented methods
- `@AllureId` values match documentation

## Project Structure

```text
API-test-demo/
├── pom.xml
├── README.md
├── src/test/java/com/apitesting/demo/
│   ├── framework/
│   │   ├── BaseTest.java
│   │   ├── CustomAllureListener.java
│   │   └── TestCaseTraceabilityTest.java
│   ├── models/
│   │   ├── Comment.java
│   │   ├── MockHttpResponse.java
│   │   ├── Post.java
│   │   └── User.java
│   ├── tests/
│   │   ├── AlbumsAPITest.java
│   │   ├── CommentsAPITest.java
│   │   ├── NetworkMockingTest.java
│   │   ├── PhotosAPITest.java
│   │   ├── PostsAPITest.java
│   │   ├── TodosAPITest.java
│   │   └── UsersAPITest.java
│   └── utils/
│       ├── APIConstants.java
│       └── ConfigReader.java
└── src/test/resources/
    ├── config.properties
    ├── TEST_CASES_DOCUMENTATION.csv
    └── UAT_SCENARIOS.md
```

## Prerequisites

- Java 11+
- Maven 3.6+
- Internet access (for live JSONPlaceholder tests)
- Port `8080` free (for WireMock tests)

## Run Commands

Run default API + mocking suites (excludes traceability guardrail test):

```powershell
mvn clean test
```

Run traceability guardrail suite only:

```powershell
mvn -Ptraceability test
```

Generate Allure report after test execution:

```powershell
mvn allure:report
```

Run a specific class:

```powershell
mvn test -Dtest=PostsAPITest
```

Run a specific method:

```powershell
mvn test -Dtest=PostsAPITest#testGetAllPosts
```

## Reporting and Logs

- Allure HTML report: `target/site/allure-maven-plugin/index.html`
- Raw Allure results: `target/allure-results/*.json`
- Execution log file: `target/test-execution.log`

## Test Case ID Integration

Test IDs are auto-resolved from `src/test/resources/TEST_CASES_DOCUMENTATION.csv` and applied to:

- log lines: `[TC-XXX-000] ...`
- Allure test name: `TC-XXX-000 - <test description>`
- Allure labels: `testCaseId`, `AS_ID`

## Key Implementation Notes

- `src/test/java/com/apitesting/demo/framework/BaseTest.java`
  - shared RestAssured setup and logger wiring
  - Allure metadata setup before each test
  - test-case ID resolution and per-test context handling
- `src/test/java/com/apitesting/demo/framework/CustomAllureListener.java`
  - removes package prefix from class names in Allure labels (`testClass`, `suite`)
- `src/test/java/com/apitesting/demo/framework/TestCaseTraceabilityTest.java`
  - validates documentation and test implementation are synchronized
- `pom.xml`
  - Surefire registers `com.apitesting.demo.framework.CustomAllureListener`
  - default run excludes `TestCaseTraceabilityTest`
  - `traceability` profile runs only the guardrail suite

## Troubleshooting

- If `mvn allure:report` says results directory not found, run tests first:

```powershell
mvn clean test
mvn allure:report
```

- If live API tests fail, verify access to `https://jsonplaceholder.typicode.com`.
- If WireMock tests fail to start, check port `8080` availability.
