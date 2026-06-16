# UAT Scenarios for API Test Automation

## Purpose
This document provides business-friendly UAT scenarios derived from the automated API tests.

## Environment
- Base API: `https://jsonplaceholder.typicode.com`
- For mock-flow scenarios: local mock server on port `8080`

## UAT-01: View all posts
- **Goal:** User can retrieve post list.
- **Steps:**
  1. Call `GET /posts`.
  2. Review response payload.
- **Expected:** 200 response with non-empty list of posts.

## UAT-02: View post details
- **Goal:** User can retrieve a single post by ID.
- **Steps:**
  1. Call `GET /posts/1`.
  2. Check post details.
- **Expected:** 200 response with `id=1`, valid title and body.

## UAT-03: Create post
- **Goal:** User can submit a new post.
- **Steps:**
  1. Submit `POST /posts` with title, body, userId.
  2. Validate creation response.
- **Expected:** 201 response and created post payload returned.

## UAT-04: Update post
- **Goal:** User can update existing post content.
- **Steps:**
  1. Submit `PUT /posts/1` with updated fields.
  2. Validate updated response body.
- **Expected:** 200 response with updated title/body.

## UAT-05: Delete post
- **Goal:** User can request post deletion.
- **Steps:**
  1. Call `DELETE /posts/1`.
  2. Validate status code.
- **Expected:** Successful delete response (200 in current API behavior).

## UAT-06: Filter posts by user
- **Goal:** User can retrieve posts for a specific user.
- **Steps:**
  1. Call `GET /posts?userId=1`.
  2. Validate all results belong to user 1.
- **Expected:** 200 response with filtered list.

## UAT-07: View users and profile details
- **Goal:** User can fetch users and user profile details.
- **Steps:**
  1. Call `GET /users`.
  2. Call `GET /users/1`.
  3. Check key fields (name, email, address, company).
- **Expected:** 200 responses with complete user data.

## UAT-08: Create and update comments
- **Goal:** User can create and modify comments.
- **Steps:**
  1. Submit `POST /comments`.
  2. Submit `PUT /comments/1`.
- **Expected:** 201 for create and 200 for update with expected data.

## UAT-09: API performance baseline
- **Goal:** Post list retrieval is responsive.
- **Steps:**
  1. Call `GET /posts`.
  2. Measure response time.
- **Expected:** Response time below 5 seconds.

## UAT-10: Error handling and mocked integration behavior
- **Goal:** Client handles controlled error and integration responses.
- **Steps:**
  1. Run mocked scenarios for 404 and 500 responses.
  2. Run mocked create-then-read sequential flow.
- **Expected:**
  - Correct status/message for 404/500.
  - Sequential flow returns expected IDs and payloads.

