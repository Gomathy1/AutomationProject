# Test Types Explained - Quick Reference

## 📁 Test Structure Overview

```
src/test/java/
├── unit/          → Unit Tests (3 files, 8 tests)
├── api/           → API Tests (2 files, 6 tests)
├── ui/            → UI Tests (2 files, 4 tests)
└── contract/      → Contract Tests (1 file, 2 tests)
    └── consumer/
```

**Old `tests/` folder has been REMOVED** - it was the old structure before reorganization.

---

## 🔬 1. Unit Tests (`src/test/java/unit/`)

### Purpose
Test individual components in **isolation** with **mocked dependencies**.

### Characteristics
- ⚡ **Very Fast** (milliseconds)
- 🔒 **No external dependencies** (no browser, no API calls)
- 🎭 **Uses Mockito** for mocking
- ✅ **Tests business logic**

### Files & Tests

| File | Tests | What It Tests |
|------|-------|---------------|
| `LoginPageUnitTest.java` | 3 | Page object methods with mocked WebElements |
| `BasePageUnitTest.java` | 2 | BasePage initialization |
| `DriverManagerUnitTest.java` | 3 | Driver setup and cleanup |

### Example
```java
@Test
public void testEnterName() {
    loginPage.enterName("John Doe");
    verify(mockNameInput, times(1)).sendKeys("John Doe");
}
```

### When to Use
- Testing page object methods
- Testing utility classes
- Testing business logic
- Before integration/UI tests

---

## 🔌 2. API Tests (`src/test/java/api/`)

### Purpose
Test **REST API endpoints** functionality and responses.

### Characteristics
- 🚀 **Fast** (seconds)
- 🌐 **Calls real APIs** (JSONPlaceholder)
- ✅ **Validates responses** (status, headers, body)
- 📊 **Uses REST Assured**

### Files & Tests

| File | Tests | What It Tests |
|------|-------|---------------|
| `UserAPITest.java` | 3 | GET, POST, DELETE user endpoints |
| `PostAPITest.java` | 3 | GET posts with query parameters, POST |

### Example
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

### When to Use
- Testing API functionality
- Validating request/response
- Testing error handling
- Performance testing (response time)

---

## 🌐 3. UI Tests (`src/test/java/ui/`)

### Purpose
Test **end-to-end user workflows** with real browsers.

### Characteristics
- 🐌 **Slower** (browser startup + navigation)
- 🌐 **Uses real browser** (Chrome/Firefox)
- 🎯 **Tests user journeys**
- 🖱️ **Uses Selenium WebDriver**

### Files & Tests

| File | Tests | What It Tests |
|------|-------|---------------|
| `LoginUITest.java` | 2 | Signup flow, login page elements |
| `HomepageUITest.java` | 2 | Homepage title, logo display |

### Example
```java
@Test
public void testUserSignup() {
    DriverManager.getDriver().get("https://automationexercise.com/");
    LoginPage loginPage = new LoginPage(DriverManager.getDriver());
    loginPage.signup("Gomathy", "gomathy.test@example.com");
}
```

### When to Use
- Testing critical user paths
- Visual/UI validation
- Cross-browser testing
- After unit and API tests pass

---

## 🤝 4. Contract Tests (`src/test/java/contract/consumer/`)

### Purpose
Test **API contracts** between consumer and provider services.

### Characteristics
- ⚡ **Fast** (no real services needed)
- 🔗 **Tests service boundaries**
- 📝 **Generates pact files**
- 🎯 **Uses Pact framework**

### Files & Tests

| File | Tests | What It Tests |
|------|-------|---------------|
| `UserServiceConsumerContractTest.java` | 2 | User API contract (success & error) |

### Example
```java
@Pact(consumer = "UserConsumer", provider = "UserProvider")
public V4Pact getUserByIdPact(PactDslWithProvider builder) {
    return builder
        .given("user with id 1 exists")
        .uponReceiving("a request to get user by id")
            .path("/users/1")
            .method("GET")
        .willRespondWith()
            .status(200)
            .body("{\"id\": 1, \"name\": \"John Doe\"}")
        .toPact(V4Pact.class);
}
```

### When to Use
- Microservices architecture
- Testing service integration
- Preventing breaking changes
- Consumer-driven contracts

---

## 🆚 Key Differences

| Aspect | Unit | API | UI | Contract |
|--------|------|-----|----|----|
| **Speed** | ⚡⚡⚡ | ⚡⚡ | 🐌 | ⚡⚡ |
| **Scope** | Single method | API endpoint | Full workflow | Service contract |
| **Dependencies** | Mocked | Real API | Real browser | Mocked provider |
| **Framework** | TestNG + Mockito | TestNG + REST Assured | TestNG + Selenium | JUnit5 + Pact |
| **When to Run** | Every commit | Every commit | Before release | On API changes |

---

## 📊 Test Pyramid

```
        /\
       /UI\          ← Few (4 tests)
      /----\
     /  API \        ← More (6 tests)
    /--------\
   / Contract \      ← More (2 tests)
  /------------\
 /    Unit      \    ← Most (8 tests)
/________________\
```

**Principle:** More unit tests, fewer UI tests.

---

## 🏃 Running Tests

### Run All Tests (TestNG - excludes contract tests)
```bash
mvn test
```
This runs: Unit, API, UI, and Security tests (via testng.xml)

### Run Specific Type
```bash
# Unit tests only
mvn test -Dtest=unit.*

# API tests only
mvn test -Dtest=api.*

# UI tests only
mvn test -Dtest=ui.*

# Security tests only
mvn test -Dtest=security.*

# Contract tests only (JUnit5 - separate execution)
mvn test -Dtest=contract.consumer.*
```

### Run Single Test Class
```bash
mvn test -Dtest=UserAPITest
```

### Run All Tests Including Contract Tests
```bash
# Run TestNG tests
mvn test

# Then run Contract tests separately
mvn test -Dtest=contract.consumer.*
```

---

## 🎯 When to Use Each Type

### Use **Unit Tests** when:
- ✅ Testing individual methods
- ✅ Testing business logic
- ✅ Fast feedback needed
- ✅ Running in CI/CD frequently

### Use **API Tests** when:
- ✅ Testing REST endpoints
- ✅ Validating API responses
- ✅ Testing data flow
- ✅ Integration testing

### Use **UI Tests** when:
- ✅ Testing critical user paths
- ✅ Visual validation needed
- ✅ Cross-browser testing
- ✅ End-to-end scenarios

### Use **Contract Tests** when:
- ✅ Building microservices
- ✅ Multiple teams/services
- ✅ Preventing breaking changes
- ✅ Service integration

---

## 📝 Summary

| Test Type | Count | Purpose | Speed |
|-----------|-------|---------|-------|
| **Unit** | 8 tests | Test components in isolation | ⚡⚡⚡ |
| **API** | 6 tests | Test REST API functionality | ⚡⚡ |
| **UI** | 4 tests | Test user workflows | 🐌 |
| **Contract** | 2 tests | Test service contracts | ⚡⚡ |
| **Total** | **20 tests** | Complete test coverage | - |

---

## 🔄 Test Execution Order

1. **Unit Tests** → Fast feedback on code changes
2. **Contract Tests** → Verify service contracts
3. **API Tests** → Validate API functionality
4. **UI Tests** → End-to-end validation

This order ensures **fast failure** and **efficient testing**.
