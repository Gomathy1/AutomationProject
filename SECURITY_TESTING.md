# Security Testing Guide

## Overview

Security testing validates that your application is protected against common vulnerabilities and follows security best practices.

## Security Test Categories

### 1. **API Security Tests** (`APISecurityTest.java`)

Tests common API vulnerabilities and attack vectors.

#### Tests Included:

**a) SQL Injection Prevention**
```java
@Test
public void testSQLInjectionPrevention() {
    String sqlInjectionPayload = "1' OR '1'='1";
    // Verifies API rejects SQL injection attempts
}
```
- **What it tests:** API input validation
- **Attack prevented:** SQL Injection (OWASP Top 10 #3)
- **Expected:** 404 or proper error handling

**b) XSS (Cross-Site Scripting) Payload Detection**
```java
@Test
public void testXSSPayloadDetection() {
    String xssPayload = "<script>alert('XSS')</script>";
    // Detects if XSS payloads are echoed back
}
```
- **What it tests:** XSS vulnerability detection
- **Attack prevented:** XSS (OWASP Top 10 #7)
- **Note:** JSONPlaceholder is a mock API and doesn't sanitize. In production, verify proper output encoding.

**c) HTTPS Enforcement**
```java
@Test
public void testHTTPSEnforcement() {
    // Verifies HTTP redirects to HTTPS
}
```
- **What it tests:** Secure communication
- **Attack prevented:** Man-in-the-Middle attacks
- **Expected:** Redirect to HTTPS or connection refused

---

### 2. **Authentication Security Tests** (`AuthenticationSecurityTest.java`)

Tests authentication and authorization mechanisms.

#### Tests Included:

**a) Unauthorized Access**
```java
@Test
public void testUnauthorizedAccess() {
    // Verifies proper access control
}
```
- **What it tests:** Access control
- **Attack prevented:** Broken Access Control (OWASP Top 10 #1)
- **Expected:** 401 Unauthorized for protected resources

**b) Invalid Token Handling**
```java
@Test
public void testInvalidTokenHandling() {
    String invalidToken = "invalid_token_12345";
    // Verifies API rejects invalid tokens
}
```
- **What it tests:** Token validation
- **Attack prevented:** Authentication bypass
- **Expected:** 401 or 403 status code

**c) Password Exposure in Response**
```java
@Test
public void testPasswordInResponse() {
    // Verifies sensitive data not exposed
}
```
- **What it tests:** Sensitive data exposure
- **Attack prevented:** Data leakage (OWASP Top 10 #2)
- **Expected:** No password fields in response

---

### 3. **Header Security Tests** (`HeaderSecurityTest.java`)

Tests security-related HTTP headers.

#### Tests Included:

**a) Security Headers Check**
```java
@Test
public void testSecurityHeaders() {
    // Checks for security headers
}
```

**Headers Validated:**
- **X-Content-Type-Options**: Prevents MIME-type sniffing
- **X-Frame-Options**: Prevents clickjacking
- **Strict-Transport-Security**: Enforces HTTPS
- **Content-Security-Policy**: Prevents XSS

**b) CORS Headers**
```java
@Test
public void testCORSHeaders() {
    // Validates CORS configuration
}
```
- **What it tests:** Cross-Origin Resource Sharing
- **Attack prevented:** Unauthorized cross-origin requests
- **Expected:** Restricted origins (not `*`)

**c) Server Header Exposure**
```java
@Test
public void testServerHeaderExposure() {
    // Checks if server info is exposed
}
```
- **What it tests:** Information disclosure
- **Attack prevented:** Reconnaissance attacks
- **Expected:** Server header hidden or generic

---

## OWASP Top 10 Coverage

| OWASP Risk | Test Coverage | Test File |
|------------|---------------|-----------|
| **A01: Broken Access Control** | ✅ | AuthenticationSecurityTest |
| **A02: Cryptographic Failures** | ✅ | APISecurityTest (HTTPS) |
| **A03: Injection** | ✅ | APISecurityTest (SQL Injection) |
| **A04: Insecure Design** | ⚠️ | Manual review needed |
| **A05: Security Misconfiguration** | ✅ | HeaderSecurityTest |
| **A06: Vulnerable Components** | ✅ | OWASP Dependency Check |
| **A07: Authentication Failures** | ✅ | AuthenticationSecurityTest |
| **A08: Data Integrity Failures** | ⚠️ | Partial coverage |
| **A09: Logging Failures** | ⚠️ | Manual review needed |
| **A10: SSRF** | ⚠️ | Not covered |

---

## Running Security Tests

### Run All Security Tests
```bash
mvn test -Dtest=security.*
```

### Run Specific Security Test
```bash
mvn test -Dtest=APISecurityTest
mvn test -Dtest=AuthenticationSecurityTest
mvn test -Dtest=HeaderSecurityTest
```

### Run in CI/CD
```yaml
- name: Run Security Tests
  run: mvn test -Dtest=security.*
```

---

## Security Testing Best Practices

### 1. **Test Early and Often**
- Run security tests in every build
- Don't wait for penetration testing
- Shift-left security approach

### 2. **Test Realistic Scenarios**
- Use real attack payloads
- Test with actual user roles
- Simulate production environment

### 3. **Combine with Tools**
- **OWASP ZAP**: Dynamic scanning
- **Dependency Check**: Vulnerable libraries
- **SonarQube**: Static code analysis
- **Snyk**: Dependency vulnerabilities

### 4. **Update Regularly**
- Keep attack payloads current
- Update to latest OWASP Top 10
- Monitor security advisories

### 5. **Don't Rely Solely on Automation**
- Manual penetration testing needed
- Security code reviews
- Threat modeling sessions

---

## Common Security Vulnerabilities to Test

### Input Validation
- ✅ SQL Injection
- ✅ XSS (Cross-Site Scripting)
- ⚠️ Command Injection
- ⚠️ Path Traversal
- ⚠️ XML External Entities (XXE)

### Authentication & Authorization
- ✅ Broken authentication
- ✅ Invalid token handling
- ✅ Unauthorized access
- ⚠️ Session fixation
- ⚠️ Privilege escalation

### Data Protection
- ✅ Sensitive data exposure
- ✅ HTTPS enforcement
- ⚠️ Weak encryption
- ⚠️ Insecure storage

### Configuration
- ✅ Security headers
- ✅ CORS misconfiguration
- ✅ Server information disclosure
- ⚠️ Default credentials
- ⚠️ Directory listing

---

## Security Test Results Interpretation

### ✅ Pass Criteria
- No SQL injection successful
- XSS payloads sanitized
- HTTPS enforced
- Proper authentication required
- Security headers present
- No sensitive data exposed

### ❌ Fail Criteria
- SQL injection executes
- XSS scripts not sanitized
- HTTP allowed without redirect
- Unauthorized access granted
- Missing security headers
- Passwords in responses

### ⚠️ Warning Criteria
- Weak security headers
- Overly permissive CORS
- Server version exposed
- Verbose error messages

---

## Integration with OWASP ZAP

For advanced security testing, integrate OWASP ZAP:

```java
// Example ZAP integration (requires ZAP running)
ClientApi zapApi = new ClientApi("localhost", 8080);
zapApi.spider.scan(targetUrl);
zapApi.ascan.scan(targetUrl);
```

---

## Security Testing Checklist

Before releasing to production:

- [ ] All security tests passing
- [ ] OWASP Dependency Check clean
- [ ] Security headers configured
- [ ] HTTPS enforced
- [ ] Authentication tested
- [ ] Authorization tested
- [ ] Input validation verified
- [ ] Sensitive data protected
- [ ] Error handling secure
- [ ] Logging implemented (no sensitive data)
- [ ] Rate limiting configured
- [ ] CORS properly configured

---

## Resources

- **OWASP Top 10**: https://owasp.org/www-project-top-ten/
- **OWASP ZAP**: https://www.zaproxy.org/
- **OWASP Dependency Check**: https://owasp.org/www-project-dependency-check/
- **Security Headers**: https://securityheaders.com/
- **REST Security Cheat Sheet**: https://cheatsheetseries.owasp.org/cheatsheets/REST_Security_Cheat_Sheet.html

---

## Summary

Security testing is **critical** and should be:
- ✅ Automated in CI/CD
- ✅ Run on every build
- ✅ Combined with manual testing
- ✅ Updated regularly
- ✅ Part of the development process

**Remember:** Security is not a feature, it's a requirement!
