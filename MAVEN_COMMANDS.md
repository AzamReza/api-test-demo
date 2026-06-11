# Maven Command Examples

## Common Maven Commands for Your Project

### 🧹 Clean & Build

```bash
# Clean build artifacts
mvn clean

# Compile source code
mvn compile

# Compile test code
mvn test-compile

# Package the project
mvn package

# Full clean build
mvn clean install

# Build without running tests
mvn clean install -DskipTests
```

### 🧪 Running Tests

```bash
# Run all tests
mvn test

# Run all tests with verbose output
mvn test -X

# Run specific test class
mvn test -Dtest=JSONPlaceholderAPITest

# Run specific test method
mvn test -Dtest=JSONPlaceholderAPITest#testGetAllPosts

# Run multiple specific tests
mvn test -Dtest=JSONPlaceholderAPITest,AdvancedAPITest

# Run tests matching pattern
mvn test -Dtest=*APITest

# Run tests and skip validation
mvn test -DskipITs
```

### 📊 Reports & Analysis

```bash
# View test reports (after running tests)
cd target/surefire-reports
dir *.txt
dir *.xml

# Generate Maven site documentation
mvn site

# View site (opens in browser after generation)
mvn site:run
```

### 🔍 Dependencies & Information

```bash
# View dependency tree
mvn dependency:tree

# Analyze dependencies
mvn dependency:analyze

# Download all dependencies
mvn dependency:resolve

# Display project information
mvn help:describe -Dcmd=test

# List all plugins
mvn help:active-profiles
```

### 🛠️ Development Workflow

```bash
# Validate project configuration
mvn validate

# Display version
mvn --version

# Help command
mvn help:help

# Get help on specific goal
mvn help:describe -Dplugin=maven-surefire-plugin

# Execute single plugin goal
mvn org.apache.maven.plugins:maven-compiler-plugin:3.11.0:compile

# Run tests in fail-fast mode
mvn test -X -fae

# Run tests with specific JDK (requires JAVA_HOME set)
mvn test
```

---

## 🎯 Real-World Examples

### Example 1: Quick Test Run
```bash
cd D:\EC-Demo-Automation\WP\API-test-demo
mvn test
```
**Output:** Runs all 25 tests and displays results

### Example 2: Run Only GET Tests
```bash
mvn test -Dtest=JSONPlaceholderAPITest#testGetAllPosts
```
**Output:** Runs only the testGetAllPosts method

### Example 3: Run AdvancedAPITest Only
```bash
mvn test -Dtest=AdvancedAPITest
```
**Output:** Runs all 9 tests in AdvancedAPITest class

### Example 4: Clean Build and Test
```bash
mvn clean test
```
**Output:** Cleans previous builds, then runs all tests

### Example 5: Build Without Tests
```bash
mvn clean install -DskipTests
```
**Output:** Builds project without running tests (faster build)

### Example 6: Package for Distribution
```bash
mvn clean package
```
**Output:** Creates JAR file in target/ directory

### Example 7: View Dependency Tree
```bash
mvn dependency:tree
```
**Output:** Shows all project dependencies hierarchy

---

## 📈 Useful Maven Properties

```bash
# Run tests with specific property
mvn test -DmyProperty=value

# Set multiple properties
mvn test -X -Dfoo=bar -Dbaz=qux

# Override default settings
mvn test -o  # Run in offline mode
mvn test -q  # Quiet mode (minimal output)
mvn test -e  # Display stack traces
mvn test -ff # Fail-fast (stop on first failure)
```

---

## 🔗 Project Specific Commands

### Run all RestAssured API Tests
```bash
mvn test -Dtest=*APITest
```

### Run POST/PUT/DELETE Tests (Create/Update/Delete)
```bash
mvn test -Dtest=JSONPlaceholderAPITest#testCreatePost
mvn test -Dtest=JSONPlaceholderAPITest#testUpdatePost
mvn test -Dtest=JSONPlaceholderAPITest#testDeletePost
```

### Run GET Tests Only
```bash
mvn test -Dtest=JSONPlaceholderAPITest#testGetAllPosts
mvn test -Dtest=JSONPlaceholderAPITest#testGetAllComments
mvn test -Dtest=JSONPlaceholderAPITest#testGetAllUsers
```

### Run Advanced Tests With Deserialization
```bash
mvn test -Dtest=AdvancedAPITest
```

### Run Specific Filtering Tests
```bash
mvn test -Dtest=JSONPlaceholderAPITest#testGetPostsByUser
mvn test -Dtest=JSONPlaceholderAPITest#testGetCommentsByPostId
```

### Run Performance Tests
```bash
mvn test -Dtest=JSONPlaceholderAPITest#testResponseTimeForGetAllPosts
```

---

## 🎓 Understanding Maven Output

### Successful Build Output
```
[INFO] BUILD SUCCESS
[INFO] Total time: 13.976 s
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
```

### Test Report Location
```
target/surefire-reports/
├── TEST-com.apitesting.demo.tests.JSONPlaceholderAPITest.xml
├── TEST-com.apitesting.demo.tests.AdvancedAPITest.xml
├── com.apitesting.demo.tests.JSONPlaceholderAPITest.txt
└── com.apitesting.demo.tests.AdvancedAPITest.txt
```

---

## 💡 Pro Tips

**Tip 1:** Use `-q` flag for cleaner output
```bash
mvn test -q
```

**Tip 2:** Use `-e` flag to see full stack traces
```bash
mvn test -e
```

**Tip 3:** Combine flags for more control
```bash
mvn clean test -DskipTests -q
```

**Tip 4:** Run tests in parallel (if supported)
```bash
mvn test -T 1C
```

**Tip 5:** Skip checksum validation (useful offline)
```bash
mvn install -o
```

---

## 🚀 Continuous Integration

### GitHub Actions Example
```yaml
- name: Run API Tests
  run: mvn clean test
```

### Jenkins Pipeline Example
```groovy
stage('Test') {
    steps {
        sh 'mvn clean test'
    }
}
```

### Azure Pipelines Example
```yaml
- script: mvn clean test
  displayName: 'Run API Tests'
```

---

## 📚 Additional Resources

- Maven Official: https://maven.apache.org/
- Maven Plugins: https://maven.apache.org/plugins/
- Maven Surefire: https://maven.apache.org/surefire/
- Settings.xml: https://maven.apache.org/settings.html

---

**Happy testing! 🎉**

