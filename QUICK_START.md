# Quick Start Guide

## 🚀 Running Tests

### Default Test Run (TestNG)
```bash
mvn test
```
**Runs:** Unit (8) + API (6) + UI (4) + Security (9) = **27 tests**

**Note:** Contract tests are excluded by default (they use JUnit5)

---

## 📦 Run Tests by Type

```bash
# Unit Tests (8 tests)
mvn test -Dtest=unit.*

# API Tests (6 tests)
mvn test -Dtest=api.*

# UI Tests (4 tests)
mvn test -Dtest=ui.*

# Security Tests (9 tests)
mvn test -Dtest=security.*

# Contract Tests (2 tests - JUnit5)
mvn test -Dtest=contract.consumer.*
```

---

## 🎯 Run Specific Test Class

```bash
mvn test -Dtest=UserAPITest
mvn test -Dtest=LoginUITest
mvn test -Dtest=APISecurityTest
```

---

## 🌐 Run UI Tests with Different Browsers

```bash
# Chrome (default)
mvn test -Dtest=ui.* -Dbrowser=chrome

# Firefox
mvn test -Dtest=ui.* -Dbrowser=firefox
```

---

## 📊 Test Summary

| Type | Files | Tests | Framework | Speed |
|------|-------|-------|-----------|-------|
| Unit | 3 | 8 | TestNG + Mockito | ⚡⚡⚡ |
| API | 2 | 6 | TestNG + REST Assured | ⚡⚡ |
| UI | 2 | 4 | TestNG + Selenium | 🐌 |
| Security | 3 | 9 | TestNG + REST Assured | ⚡⚡ |
| Contract | 1 | 2 | JUnit5 + Pact | ⚡⚡ |
| **Total** | **11** | **29** | - | - |

---

## 🔧 Common Issues

### Issue: Contract tests fail
**Solution:** Run them separately with JUnit5:
```bash
mvn test -Dtest=contract.consumer.*
```

### Issue: UI tests fail
**Solution:** Ensure browser drivers are available (WebDriverManager handles this automatically)

### Issue: Build fails with Java version error
**Solution:** Project requires Java 11. Check your Java version:
```bash
java -version
```

---

## 📁 Test Structure

```
src/test/java/
├── unit/          → Fast, isolated tests with mocks
├── api/           → REST API endpoint tests
├── ui/            → Browser-based E2E tests
├── security/      → Security vulnerability tests
└── contract/      → Service contract tests (JUnit5)
```

---

## 📖 Documentation

- **TEST_TYPES_EXPLAINED.md** - Detailed explanation of each test type
- **CONTRACT_TESTING.md** - Contract testing guide
- **SECURITY_TESTING.md** - Security testing guide
- **TEST_STRUCTURE.md** - Project structure details

---

## ⚡ Quick Commands

```bash
# Install dependencies
mvn clean install

# Run all TestNG tests
mvn test

# Run all tests including contract tests
mvn test && mvn test -Dtest=contract.consumer.*

# Clean and run tests
mvn clean test

# Skip tests during build
mvn clean install -DskipTests
```

---

## 🎯 Recommended Test Order

1. **Unit Tests** → Fastest feedback
2. **Security Tests** → Check vulnerabilities
3. **API Tests** → Validate endpoints
4. **Contract Tests** → Verify service contracts
5. **UI Tests** → End-to-end validation

This ensures **fast failure** and efficient testing!
