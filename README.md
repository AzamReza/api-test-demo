# API Testing Automation Demo

A comprehensive REST API testing automation framework using **RestAssured**, **Java**, and **Maven**.

## 📋 Project Overview

This project demonstrates automated API testing for the **JSONPlaceholder** API - a free, public API perfect for testing.

### **What is JSONPlaceholder?**
JSONPlaceholder is a free online REST API that you can use whenever you need some fake data. It's great for:
- Client-side testing
- Server-side testing
- Building mock data before building your API
- Learning REST API concepts

**API URL:** https://jsonplaceholder.typicode.com

### **Available Resources:**
- **Posts** - Blog posts
- **Comments** - Comments on posts
- **Users** - User information
- **Todos** - Todo items
- **Albums** - Photo albums
- **Photos** - Photos in albums

## 🛠️ Technology Stack

- **Language:** Java 11+
- **Testing Framework:** JUnit 4
- **API Testing Library:** RestAssured 5.3.2
- **Build Tool:** Maven 3.6+
- **Assertion Library:** Hamcrest Matchers
- **JSON Parser:** Gson
- **IDE:** IntelliJ IDEA Recommended

## 📦 Project Structure

```
api-test-automation/
├── src/
│   ├── test/
│   │   └── java/
│   │       └── com/apitesting/demo/
│   │           ├── models/
│   │           │   ├── Post.java
│   │           │   ├── User.java
│   │           │   └── Comment.java
│   │           ├── tests/
│   │           │   ├── BaseTest.java
│   │           │   ├── JSONPlaceholderAPITest.java
│   │           │   └── AdvancedAPITest.java
│   │           └── utils/
│   │               └── APIConstants.java
├── pom.xml
└── README.md
```

## 📝 Test Cases Included

### **JSONPlaceholderAPITest.java**
- ✅ Get all posts
- ✅ Get specific post
- ✅ Get post comments
- ✅ Get all comments
- ✅ Get specific comment
- ✅ Get all users
- ✅ Get specific user
- ✅ Create new post (POST)
- ✅ Create new comment (POST)
- ✅ Update post (PUT)
- ✅ Update comment (PUT)
- ✅ Delete post (DELETE)
- ✅ Delete comment (DELETE)
- ✅ Query parameter filtering by User ID
- ✅ Query parameter filtering by Post ID
- ✅ Response time validation

### **AdvancedAPITest.java**
- ✅ Deserialize JSON response to Java objects
- ✅ Object-level assertions
- ✅ Array/Collection handling
- ✅ Complex filtering scenarios
- ✅ Response header validation
- ✅ Response body validation
- ✅ Data consistency checks

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher installed
- Maven 3.6 or higher installed
- Internet connection (to access JSONPlaceholder API)

### Installation

1. **Clone/Download the project:**
   ```bash
   cd D:\EC-Demo-Automation\WP\API-test-demo
   ```

2. **Install dependencies:**
   ```bash
   mvn clean install
   ```

3. **Run all tests:**
   ```bash
   mvn test
   ```

4. **Run specific test class:**
   ```bash
   mvn test -Dtest=JSONPlaceholderAPITest
   ```

5. **Run specific test method:**
   ```bash
   mvn test -Dtest=JSONPlaceholderAPITest#testGetAllPosts
   ```

## 📊 Sample Test Results

When you run the tests, you'll see output like:
```
✓ Test Get All Posts - PASSED
✓ Test Get Specific Post - PASSED
✓ Test Get Post Comments - PASSED
✓ Test Create Post - PASSED
✓ Test Update Post - PASSED
✓ Test Delete Post - PASSED
✓ Test Response Time for Get All Posts - PASSED
```

## 🔑 Key RestAssured Features Used

### 1. **GET Requests**
```java
given()
    .accept("application/json")
.when()
    .get("/posts/1")
.then()
    .statusCode(200)
    .body("id", equalTo(1));
```

### 2. **POST Requests**
```java
given()
    .contentType("application/json")
    .body(requestBody)
.when()
    .post("/posts")
.then()
    .statusCode(201);
```

### 3. **Query Parameters**
```java
given()
    .queryParam("userId", 1)
.when()
    .get("/posts")
.then()
    .statusCode(200);
```

### 4. **Response Deserialization**
```java
Post post = given()
.when()
    .get("/posts/1")
.as(Post.class);
```

### 5. **Assertions with Hamcrest Matchers**
```java
.body("size()", greaterThan(0))
.body("[0].title", notNullValue())
.body("[0].id", isA(Integer.class))
```

## 📚 RestAssured Documentation

For more information, visit:
- [RestAssured Official Documentation](https://rest-assured.io/)
- [RestAssured GitHub](https://github.com/rest-assured/rest-assured)

## JSONPlaceholder API Documentation

Visit: https://jsonplaceholder.typicode.com

Available endpoints:
- GET `/posts` - Get all posts
- GET `/posts/{id}` - Get specific post
- POST `/posts` - Create a new post
- PUT `/posts/{id}` - Update a post
- DELETE `/posts/{id}` - Delete a post
- GET `/comments` - Get all comments
- GET `/comments/{id}` - Get specific comment
- GET `/users` - Get all users
- GET `/users/{id}` - Get specific user

## 🎯 Best Practices Implemented

1. **Base Test Class** - Common setup in `BaseTest.java`
2. **Constants** - Centralized in `APIConstants.java`
3. **Data Models** - Strong typing with `Post.java`, `User.java`, `Comment.java`
4. **Modular Tests** - Organized into logical test classes
5. **Descriptive Assertions** - Using Hamcrest matchers for clear validation
6. **Response Logging** - Enabled via `enableLoggingOfRequestAndResponseIfValidationFails()`
7. **Organized Package Structure** - Proper separation of concerns

## 💡 Tips for Extending Tests

1. **Add new test methods** to existing test classes
2. **Create new model classes** for additional API resources (Albums, Todos, Photos)
3. **Use data builders** for complex test data
4. **Implement parametrized tests** for testing multiple scenarios
5. **Add test reports** using Maven plugins

## 🔧 Troubleshooting

### Port/Connection Issues
- The JSONPlaceholder API is cloud-based, ensure your internet connection is active

### Maven Build Issues
```bash
mvn clean install
```

### Test Failures
- Check API endpoint availability at https://jsonplaceholder.typicode.com
- Verify network connectivity
- Review test output logs

## 📄 License

This project is open source and available for educational and demo purposes.

## 👨‍💻 Author

Created as a demonstration project for API automation testing.

---

**Happy Testing! 🚀**

