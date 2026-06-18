# API Testing Automation Demo

Single source of truth for this repository.

## Overview

This project is a Java + Maven API automation framework for `https://jsonplaceholder.typicode.com` with:

- REST API tests using RestAssured + JUnit 4
- Network mocking tests using WireMock
- Allure reporting
- Test case ID traceability from `src/test/resources/TEST_CASES_DOCUMENTATION.md`

## Current Test Suites

- `JSONPlaceholderAPITest` (16 tests): CRUD, filtering, response-time checks
- `AdvancedAPITest` (9 tests): deserialization, object assertions, consistency checks
- `NetworkMockingTest` (10 tests): WireMock GET/POST/PUT/DELETE, errors, verification
- `CoverageAPITest` (14 tests): contract checks, negative scenarios, query behavior, PATCH, operational headers

Total automated tests: **49**

## Project Structure

```text
API-test-demo/
├── pom.xml
├── README.md
├── src/test/java/com/apitesting/demo/
│   ├── models/
│   │   ├── Comment.java
│   │   ├── Post.java
│   │   └── User.java
│   ├── tests/
│   │   ├── AdvancedAPITest.java
│   │   ├── BaseTest.java
│   │   ├── CoverageAPITest.java
│   │   ├── CustomAllureListener.java
│   │   ├── JSONPlaceholderAPITest.java
│   │   └── NetworkMockingTest.java
│   └── utils/
│       └── APIConstants.java
└── src/test/resources/
    └── TEST_CASES_DOCUMENTATION.md
```

## Prerequisites

- Java 11+
- Maven 3.6+
- Internet access (for live JSONPlaceholder tests)
- Port `8080` free (for WireMock tests)

## Run Commands

```bash
mvn clean test
mvn allure:report
```

Run a specific class:

```bash
mvn test -Dtest=JSONPlaceholderAPITest
```

Run a specific method:

```bash
mvn test -Dtest=JSONPlaceholderAPITest#testGetAllPosts
```

## Reporting and Logs

- Allure HTML report: `target/site/allure-maven-plugin/index.html`
- Raw Allure results: `target/allure-results/*.json`
- Execution log file: `target/test-execution.log`

## Test Case ID Integration

Test IDs are auto-resolved from `src/test/resources/TEST_CASES_DOCUMENTATION.md` and applied to:

- Log lines (prefix format): `[TC-XXX-000] ...`
- Allure test name: `TC-XXX-000 - <test description>`
- Allure label: `testCaseId`

Example log format:

```text
[TC-ADV-007] Start test: Response Body Validation
[TC-ADV-007] Test Response Body Validation - PASSED
```

## Key Implementation Notes

- `BaseTest.java`
  - common setup for RestAssured and logging
  - Allure metadata setup before each test
  - test case ID mapping and per-test context handling
- `CustomAllureListener.java`
  - removes package prefix from class names in Allure labels (`testClass`, `suite`)
- `pom.xml`
  - Surefire configured with Allure listener and `allure.results.directory`
  - Allure report plugin configured

## Troubleshooting

- If `mvn allure:report` says results directory not found, run tests first:

```bash
mvn clean test
mvn allure:report
```

- If live API tests fail, verify network access to `https://jsonplaceholder.typicode.com`.

- If WireMock tests fail to start, check port `8080` availability.

## Maintenance Rule

Keep root-level docs minimal: this `README.md` is the only root markdown file.

