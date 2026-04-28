# Test Structure Documentation

## Overview
This project has a clear separation between **Unit Tests**, **API Tests**, and **UI Tests**.

## Test Types

### Unit Tests (`src/test/java/unit/`)
Fast, isolated tests that mock dependencies and test individual components without browser automation.

**Files:**
- `LoginPageUnitTest.java` - Tests LoginPage methods with mocked WebElements
- `BasePageUnitTest.java` - Tests BasePage initialization and configuration
- `DriverManagerUnitTest.java` - Tests DriverManager functionality

**Characteristics:**
- ✅ Fast execution (no browser startup)
- ✅ Use Mockito for mocking
- ✅ Test business logic in isolation
- ✅ Run in CI/CD without browser dependencies

**Example:**
```java
@Test
public void testEnterName() {
    loginPage.enterName("John Doe");
    verify(mockNameInput, times(1)).sendKeys("John Doe");
}
```

### API Tests (`src/test/java/api/`)
REST API tests using REST Assured framework to test backend services.

**Files:**
- `BaseAPITest.java` - Base class with API configuration
- `UserAPITest.java` - User API endpoint tests (GET, POST, PUT, PATCH, DELETE)
- `PostAPITest.java` - Post API endpoint tests with query parameters
- `CommentAPITest.java` - Comment API endpoint tests

**Characteristics:**
- 🔌 Tests REST API endpoints
- 🔌 Fast execution (no browser)
- 🔌 Validates response status, headers, and body
- 🔌 Uses JSONPlaceholder API for testing

**Example:**
```java
@Test
public void testGetUserById() {
    given()
        .spec(requestSpec)
    .when()
        .get("/users/1")
    .then()
        .statusCode(200)
        .body("id", equalTo(1))
        .body("email", containsString("@"));
}
```

### UI Tests (`src/test/java/ui/`)
End-to-end UI tests that use real browsers and test complete user workflows.

**Files:**
- `BaseUITest.java` - Base class with setup/teardown
- `LoginUITest.java` - Login/signup flow tests
- `HomepageUITest.java` - Homepage verification tests

**Characteristics:**
- 🌐 Uses real browser (Chrome/Firefox)
- 🌐 Tests complete user journeys
- 🌐 Slower execution
- 🌐 Tests actual UI interactions

**Example:**
```java
@Test
public void testUserSignup() {
    DriverManager.getDriver().get("https://automationexercise.com/");
    LoginPage loginPage = new LoginPage(DriverManager.getDriver());
    loginPage.signup("Gomathy", "gomathy.test@example.com");
}
```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Only Unit Tests
```bash
mvn test -Dtest=unit.*
```

### Run Only API Tests
```bash
mvn test -Dtest=api.*
```

### Run Only UI Tests
```bash
mvn test -Dtest=ui.*
```

### Run Specific Test Class
```bash
mvn test -Dtest=UserAPITest
```

## TestNG Configuration

The `testng.xml` file is organized into three test groups:

```xml
<suite name="Automation Test Suite">
    <test name="Unit Tests">
        <classes>
            <class name="unit.LoginPageUnitTest"/>
            <class name="unit.BasePageUnitTest"/>
            <class name="unit.DriverManagerUnitTest"/>
        </classes>
    </test>
    
    <test name="API Tests">
        <classes>
            <class name="api.UserAPITest"/>
            <class name="api.PostAPITest"/>
            <class name="api.CommentAPITest"/>
        </classes>
    </test>
    
    <test name="UI Tests">
        <classes>
            <class name="ui.LoginUITest"/>
            <class name="ui.HomepageUITest"/>
        </classes>
    </test>
</suite>
```

## CI/CD Integration

The GitHub Actions workflow (`pr-comment.yml`) now correctly identifies:
- **Unit Tests:** Files in `src/test/java/unit/` matching `*Test.java`
- **API Tests:** Files in `src/test/java/api/` matching `*Test.java`
- **UI Tests:** Files in `src/test/java/ui/` matching `*Test.java`

## Test Count Summary

| Type | Count | Test Methods | Location |
|------|-------|--------------|----------|
| Unit Tests | 3 | 18 | `src/test/java/unit/` |
| API Tests | 4 | 23 | `src/test/java/api/` |
| UI Tests | 3 | 4 | `src/test/java/ui/` |
| **Total** | **10** | **45** | - |

## Best Practices

1. **Unit tests should:**
   - Be fast (< 1 second per test)
   - Not depend on external systems
   - Use mocks for dependencies
   - Test one thing at a time

2. **E2E tests should:**
   - Test critical user paths
   - Use Page Object Model
   - Have meaningful assertions
   - Clean up after execution

3. **Naming conventions:**
   - Unit tests: `*UnitTest.java`
   - E2E tests: `*Test.java`
