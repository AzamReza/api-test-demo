# AGENTS.md

## Project Snapshot
- This is a **test-only Java Maven repo** (no `src/main`), centered on API validation under `src/test/java/com/apitesting/demo`.
- Two execution modes coexist: live API tests against JSONPlaceholder and local mocked flows via WireMock.
- Core test classes: `JSONPlaceholderAPITest`, `AdvancedAPITest`, `CoverageAPITest`, `NetworkMockingTest`.

## Architecture and Data Flow (What matters)
- `BaseTest` is the framework spine: it sets `RestAssured.baseURI`, wires `AllureRestAssured`, configures file logging, and injects test-case metadata.
- Runtime config is centralized in `config.properties` and loaded through `ConfigReader`; constants are exposed by `APIConstants`.
- Test-case traceability is document-driven: `TEST_CASES_DOCUMENTATION.csv` is parsed at runtime to map test methods -> `TC-*` IDs.
- Allure naming/labeling is customized in two places: `BaseTest` (test name + `testCaseId` label) and `CustomAllureListener` (strip package names from suite/class labels).
- `TestCaseTraceabilityTest` acts as a guardrail: it fails if documentation and implemented tests drift.

## Critical Workflows
- Run full suite:
  - `mvn clean test`
- Generate Allure report after tests:
  - `mvn allure:report`
- Run one class or one method:
  - `mvn test -Dtest=JSONPlaceholderAPITest`
  - `mvn test -Dtest=JSONPlaceholderAPITest#testGetAllPosts`
- Key outputs:
  - `target/surefire-reports`
  - `target/allure-results`
  - `target/site/allure-maven-plugin/index.html`
  - `target/test-execution.log`

## Conventions You Must Follow in Changes
- Keep new tests under `src/test/java/com/apitesting/demo/tests` and make classes extend `BaseTest`.
- Resolve endpoints/status/content-type via `APIConstants` (not hardcoded literals).
- Add `BaseTest.logTest("...")` at meaningful milestones so logs and Allure steps stay aligned.
- If adding/changing tests in tracked suites, update `src/test/resources/TEST_CASES_DOCUMENTATION.csv` with matching method names and IDs.
- If using `@AllureId`, ensure it exactly matches the corresponding `TC-*` entry in documentation.
- Keep suite behavior deterministic where possible (see `CoverageAPITest` patterns like `_limit`, `_sort`, explicit cardinality checks).

## Integration Boundaries and Dependencies
- External live dependency: `https://jsonplaceholder.typicode.com` (network required).
- Local integration dependency: WireMock on `localhost:8080`; port conflicts break `NetworkMockingTest`.
- Tooling stack from `pom.xml`: TestNG, RestAssured, WireMock, Allure (`allure-testng`, `allure-rest-assured`), AspectJ weaving via Surefire `argLine`.
- Surefire is preconfigured to run `*Test.java` and `*Tests.java` and to register `com.apitesting.demo.framework.CustomAllureListener`.

## File References for Fast Orientation
- Framework setup: `src/test/java/com/apitesting/demo/framework/BaseTest.java`
- Reporting customization: `src/test/java/com/apitesting/demo/framework/CustomAllureListener.java`
- Traceability enforcement: `src/test/java/com/apitesting/demo/framework/TestCaseTraceabilityTest.java`
- Runtime config/constants: `src/test/resources/config.properties`, `src/test/java/com/apitesting/demo/utils/APIConstants.java`
- Canonical test mapping: `src/test/resources/TEST_CASES_DOCUMENTATION.csv`

