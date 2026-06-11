# Quick Start Guide

## Running the Tests

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=JSONPlaceholderAPITest
```

### Run specific test method
```bash
mvn test -Dtest=JSONPlaceholderAPITest#testGetAllPosts
```

### Run with verbose output
```bash
mvn test -X
```

### Skip tests during build
```bash
mvn clean install -DskipTests
```

## Project Architecture

### Test Classes
1. **JSONPlaceholderAPITest.java** (16 tests)
   - Basic CRUD operations (GET, POST, PUT, DELETE)
   - Query parameter filtering
   - Response validation
   - Response time testing

2. **AdvancedAPITest.java** (9 tests)
   - JSON deserialization to Java objects
   - Array/Collection handling
   - Complex filtering scenarios
   - Response header validation
   - Data consistency checks

### Supporting Classes
- **BaseTest.java** - Common setup for all tests
- **APIConstants.java** - Constants for endpoints and status codes
- **Post.java** - Post model with POJO structure
- **User.java** - User model with POJO structure
- **Comment.java** - Comment model with POJO structure

## API Under Test: JSONPlaceholder

**Base URL:** https://jsonplaceholder.typicode.com

### Available Resources:
- `/posts` - Blog posts (100 posts)
- `/comments` - Comments on posts (500 comments)
- `/users` - User information (10 users)
- `/todos` - Todo items (200 todos)
- `/albums` - Photo albums (10 albums)
- `/photos` - Photos (5000 photos)

## Test Results Summary

✅ **25/25 Tests PASSING**

### Test Coverage:
- GET requests with assertions: ✅
- POST requests (Create): ✅
- PUT requests (Update): ✅
- DELETE requests: ✅
- Query parameters: ✅
- JSON deserialization: ✅
- Response headers validation: ✅
- Response body assertions: ✅
- Response time assertions: ✅
- Data consistency: ✅

## Maven Commands

```bash
# Clean build
mvn clean

# Compile project
mvn compile

# Package project
mvn package

# Install locally
mvn install

# Clean install and run tests
mvn clean test

# Run tests with surefire
mvn surefire:test

# Generate site documentation
mvn site
```

## Dependencies

- **RestAssured** v5.3.2 - REST API testing
- **JUnit** v4.13.2 - Testing framework
- **Hamcrest** v2.2 - Assertion matchers
- **Gson** v2.10.1 - JSON parsing
- **Log4j** v1.2.17 - Logging

## Tips for Extending Tests

1. Add more test methods to existing test classes
2. Create new model classes for Albums, Todos, Photos
3. Implement parametrized tests for multiple scenarios
4. Add test data builders for complex objects
5. Use custom matchers for specialized assertions

## Troubleshooting

### Network Issues
- Verify internet connection
- Check if JSONPlaceholder API is accessible: https://jsonplaceholder.typicode.com

### Build Issues
- Run `mvn clean` to remove cached artifacts
- Update Maven: `mvn -version`
- Check Java version: `java -version`

### Test Failures
- Check API endpoint in test logs
- Verify response format matches expected structure
- Review assertion matchers for correctness

## Performance Metrics (from test runs)

- Average test execution time: ~5-7 seconds for 25 tests
- API response time: < 1 second (well under 5 second limit)
- All tests execute without network timeouts

---

**Good luck with your API testing automation! 🚀**

