# API Test Cases Documentation

## 1. Scope
This document describes the automated API test cases implemented in:
- `src/test/java/com/apitesting/demo/tests/JSONPlaceholderAPITest.java`
- `src/test/java/com/apitesting/demo/tests/AdvancedAPITest.java`
- `src/test/java/com/apitesting/demo/tests/NetworkMockingTest.java`

## 2. Test Environment and Preconditions
1. Java 11+ and Maven are installed.
2. Internet is available for live API tests against `https://jsonplaceholder.typicode.com`.
3. No proxy/firewall rule blocks outbound HTTPS requests.
4. Port `8080` is free for WireMock tests.

## 3. Common Execution Steps
1. Open terminal in project root: `D:\EC-Demo-Automation\WP\API-test-demo`
2. Run automated tests:
   - `mvn clean test`
3. Review test report in console and under `target/surefire-reports`.

---

## 4. JSONPlaceholder API Test Cases (`JSONPlaceholderAPITest`)

### TC-JP-001: Get all posts
- **Method:** `testGetAllPosts`
- **Steps:**
  1. Send `GET /posts` with `Accept: application/json`.
  2. Validate HTTP status and payload structure.
- **Expected Result:**
  - Status `200`
  - Content type `application/json`
  - Response array size greater than 0
  - First item contains `userId`, `id`, `title`, `body`

### TC-JP-002: Get specific post by ID
- **Method:** `testGetSpecificPost`
- **Steps:**
  1. Send `GET /posts/1`.
  2. Validate response fields.
- **Expected Result:**
  - Status `200`
  - `id=1`, `userId=1`
  - `title` and `body` are not null

### TC-JP-003: Get comments for a post
- **Method:** `testGetPostComments`
- **Steps:**
  1. Send `GET /posts/1/comments`.
  2. Validate list and comment fields.
- **Expected Result:**
  - Status `200`
  - Non-empty response array
  - First comment contains `postId`, `id`, `name`, `email`, `body`

### TC-JP-004: Get all comments
- **Method:** `testGetAllComments`
- **Steps:**
  1. Send `GET /comments`.
  2. Validate response size.
- **Expected Result:**
  - Status `200`
  - Non-empty response array

### TC-JP-005: Get specific comment by ID
- **Method:** `testGetSpecificComment`
- **Steps:**
  1. Send `GET /comments/1`.
  2. Validate core fields.
- **Expected Result:**
  - Status `200`
  - `id=1`
  - `postId`, `name`, `email` are present

### TC-JP-006: Get all users
- **Method:** `testGetAllUsers`
- **Steps:**
  1. Send `GET /users`.
  2. Validate user object structure.
- **Expected Result:**
  - Status `200`
  - Non-empty response array
  - First item contains `id`, `name`, `email`, `address`, `phone`, `website`, `company`

### TC-JP-007: Get specific user by ID
- **Method:** `testGetSpecificUser`
- **Steps:**
  1. Send `GET /users/1`.
  2. Validate selected fields.
- **Expected Result:**
  - Status `200`
  - `id=1`
  - `name` and `email` are not null

### TC-JP-008: Create post
- **Method:** `testCreatePost`
- **Steps:**
  1. Prepare JSON body with `title`, `body`, `userId`.
  2. Send `POST /posts` with `Content-Type: application/json`.
  3. Validate created response body.
- **Expected Result:**
  - Status `201`
  - `id` is returned
  - `title`, `body`, and `userId` match request

### TC-JP-009: Create comment
- **Method:** `testCreateComment`
- **Steps:**
  1. Prepare JSON body with `postId`, `name`, `email`, `body`.
  2. Send `POST /comments`.
  3. Validate response payload.
- **Expected Result:**
  - Status `201`
  - `id` is returned
  - `postId=1`, `name` and `email` match request

### TC-JP-010: Update post
- **Method:** `testUpdatePost`
- **Steps:**
  1. Prepare full update payload for post `id=1`.
  2. Send `PUT /posts/1`.
  3. Validate updated values.
- **Expected Result:**
  - Status `200`
  - `id=1`
  - `title` and `body` reflect updated values

### TC-JP-011: Update comment
- **Method:** `testUpdateComment`
- **Steps:**
  1. Prepare comment update payload for `id=1`.
  2. Send `PUT /comments/1`.
  3. Validate key fields.
- **Expected Result:**
  - Status `200`
  - `id=1`
  - `name` reflects updated value

### TC-JP-012: Delete post
- **Method:** `testDeletePost`
- **Steps:**
  1. Send `DELETE /posts/1`.
  2. Validate delete response code.
- **Expected Result:**
  - Status `200`

### TC-JP-013: Delete comment
- **Method:** `testDeleteComment`
- **Steps:**
  1. Send `DELETE /comments/1`.
  2. Validate delete response code.
- **Expected Result:**
  - Status `200`

### TC-JP-014: Filter posts by user
- **Method:** `testGetPostsByUser`
- **Steps:**
  1. Send `GET /posts?userId=1`.
  2. Validate filtered list.
- **Expected Result:**
  - Status `200`
  - Non-empty list
  - First item has `userId=1`

### TC-JP-015: Filter comments by post ID
- **Method:** `testGetCommentsByPostId`
- **Steps:**
  1. Send `GET /comments?postId=1`.
  2. Validate filtered response.
- **Expected Result:**
  - Status `200`
  - Non-empty list
  - First item has `postId=1`

### TC-JP-016: Response-time validation
- **Method:** `testResponseTimeForGetAllPosts`
- **Steps:**
  1. Send `GET /posts`.
  2. Measure and validate response time.
- **Expected Result:**
  - Status `200`
  - Response time less than 5000 ms

---

## 5. Advanced API Test Cases (`AdvancedAPITest`)

### TC-ADV-001: Deserialize single post to Java object
- **Method:** `testGetPostAsObject`
- **Steps:**
  1. Send `GET /posts/1`.
  2. Deserialize response to `Post` model.
  3. Validate object fields.
- **Expected Result:**
  - `Post` object is not null
  - `id=1`, `userId=1`
  - `title` and `body` are not null

### TC-ADV-002: Deserialize single user to Java object
- **Method:** `testGetUserAsObject`
- **Steps:**
  1. Send `GET /users/1`.
  2. Deserialize response to `User` model.
  3. Validate required fields.
- **Expected Result:**
  - `User` object is not null
  - `id=1`
  - `name`, `email`, `username` are not null

### TC-ADV-003: Create post using object serialization
- **Method:** `testCreatePostUsingObject`
- **Steps:**
  1. Build `Post` object in code.
  2. Send `POST /posts` with object body.
  3. Deserialize response and validate values.
- **Expected Result:**
  - Status `201`
  - Returned object is not null
  - `title`, `body`, and `userId` match input object

### TC-ADV-004: Deserialize array of posts
- **Method:** `testMultiplePostsRetrieval`
- **Steps:**
  1. Send `GET /posts`.
  2. Deserialize response to `Post[]`.
  3. Validate array size and each object.
- **Expected Result:**
  - Array length > 0 and at least 10
  - Every post has non-null `id`, `title`, `body`
  - `userId > 0` for each post

### TC-ADV-005: Filter and deserialize posts by user ID
- **Method:** `testFilterPostsByUserId`
- **Steps:**
  1. Send `GET /posts?userId=2`.
  2. Deserialize to `Post[]`.
  3. Verify all returned posts belong to user 2.
- **Expected Result:**
  - Non-empty array
  - Every item has `userId=2`

### TC-ADV-006: Validate response headers
- **Method:** `testResponseHeaders`
- **Steps:**
  1. Send `GET /posts`.
  2. Verify response headers.
- **Expected Result:**
  - Status `200`
  - `Content-Type` contains `application/json`
  - `Transfer-Encoding` is present

### TC-ADV-007: Validate response body schema basics
- **Method:** `testResponseBodyValidation`
- **Steps:**
  1. Send `GET /posts/1`.
  2. Validate field types and required values.
- **Expected Result:**
  - Status `200`
  - `id=1`
  - `userId` present
  - `title` and `body` are strings

### TC-ADV-008: Query-parameter filtering with object assertions
- **Method:** `testQueryParameterWithFiltering`
- **Steps:**
  1. Set `userId=1` query parameter.
  2. Send `GET /posts` and deserialize to `Post[]`.
  3. Validate all returned records.
- **Expected Result:**
  - Non-empty array
  - Every post has `userId=1`

### TC-ADV-009: Cross-resource data consistency
- **Method:** `testDataConsistency`
- **Steps:**
  1. Fetch `Post` from `GET /posts/1`.
  2. Fetch comments using `GET /comments?postId=<post.id>`.
  3. Validate related comments exist.
- **Expected Result:**
  - Post object is returned
  - Comment list size is greater than 0

---

## 6. Network Mocking Test Cases (`NetworkMockingTest`)

### TC-MOCK-001: Mocked GET user data
- **Method:** `testMockedGetUserData`
- **Steps:**
  1. Start WireMock and stub `GET /api/users/1` with static JSON.
  2. Send request through RestAssured to mock server.
  3. Validate mocked response.
- **Expected Result:**
  - Status `200`
  - JSON response contains expected `id`, `name`, `email`, `phone`

### TC-MOCK-002: Mocked GET posts list
- **Method:** `testMockedGetPostsList`
- **Steps:**
  1. Stub `GET /api/posts` with array payload.
  2. Send request to mocked endpoint.
  3. Validate list size and values.
- **Expected Result:**
  - Status `200`
  - Array size `2`
  - Post IDs and titles match stub

### TC-MOCK-003: Mocked POST create user
- **Method:** `testMockedCreateUser`
- **Steps:**
  1. Stub `POST /api/users` with `201` response body.
  2. Send JSON request body.
  3. Validate response fields.
- **Expected Result:**
  - Status `201`
  - Returned `id`, `name`, `email` match stub

### TC-MOCK-004: Mocked PUT update user
- **Method:** `testMockedUpdateUser`
- **Steps:**
  1. Stub `PUT /api/users/1` with `Content-Type` header condition.
  2. Send update payload.
  3. Validate updated values in response.
- **Expected Result:**
  - Status `200`
  - Returned `id=1`
  - Updated `name` and `email` match stub

### TC-MOCK-005: Mocked DELETE user
- **Method:** `testMockedDeleteUser`
- **Steps:**
  1. Stub `DELETE /api/users/1` with no-content response.
  2. Send delete request.
- **Expected Result:**
  - Status `204`

### TC-MOCK-006: Verify request hit count on mock server
- **Method:** `testVerifyMockServerRequest`
- **Steps:**
  1. Stub `GET /api/users/1`.
  2. Execute request once.
  3. Verify endpoint was called exactly once in WireMock.
- **Expected Result:**
  - Request verification passes (`exactly(1)`)

### TC-MOCK-007: Mocked 404 error response
- **Method:** `testMockedErrorResponse404`
- **Steps:**
  1. Stub `GET /api/users/999` with `404` and error payload.
  2. Send request and validate error body.
- **Expected Result:**
  - Status `404`
  - Response contains `error = "User not found"`

### TC-MOCK-008: Mocked 500 error response
- **Method:** `testMockedErrorResponse500`
- **Steps:**
  1. Stub `POST /api/users` with `500` and error payload.
  2. Send request body and validate response.
- **Expected Result:**
  - Status `500`
  - Response contains `error = "Internal Server Error"`

### TC-MOCK-009: Mocked GET with query parameter
- **Method:** `testMockedGetWithQueryParameters`
- **Steps:**
  1. Stub `GET /api/posts` path endpoint.
  2. Send request with query parameter `userId=1`.
  3. Validate returned payload.
- **Expected Result:**
  - Status `200`
  - Response list size `1`
  - First item has `userId=1`

### TC-MOCK-010: Mocked multiple sequential requests
- **Method:** `testMockedMultipleSequentialRequests`
- **Steps:**
  1. Stub `POST /api/users` for create flow.
  2. Stub `GET /api/users/101` for read-after-create flow.
  3. Execute POST then GET in sequence.
  4. Validate both responses.
- **Expected Result:**
  - First call returns `201` with `id=101`
  - Second call returns `200` with `id=101` and expected `email`

---

## 7. Traceability Summary
- **Total documented automated test cases:** 35
  - JSONPlaceholder API Suite: 16
  - Advanced API Suite: 9
  - Network Mocking Suite: 10

