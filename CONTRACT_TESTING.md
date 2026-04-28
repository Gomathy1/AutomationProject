# Contract Testing Guide

## What is Contract Testing?

Contract testing is a testing approach that verifies the interactions between services (consumer and provider) by checking that they adhere to a shared contract. This is especially important in microservices architectures.

## Why Contract Testing?

### Traditional Problems
- **Integration tests are slow** - Require full environment setup
- **End-to-end tests are brittle** - Break when any service changes
- **Mocking is incomplete** - Mock responses may not match real API

### Contract Testing Benefits
✅ **Fast** - No need for full environment  
✅ **Reliable** - Tests actual contract, not implementation  
✅ **Early detection** - Find breaking changes before deployment  
✅ **Independent** - Consumer and provider can test separately  
✅ **Documentation** - Contracts serve as living documentation

## Contract Testing vs Other Testing Types

| Type | Speed | Scope | When to Use |
|------|-------|-------|-------------|
| **Unit Tests** | ⚡ Very Fast | Single component | Test business logic |
| **Contract Tests** | ⚡ Fast | Service boundaries | Test API contracts |
| **API Tests** | 🏃 Medium | Full API | Test API functionality |
| **UI Tests** | 🐌 Slow | Full system | Test user journeys |

## Pact Framework

This project uses **Pact** for contract testing, which follows a consumer-driven approach:

1. **Consumer** defines expectations (what it needs from the provider)
2. **Pact file** is generated with the contract
3. **Provider** verifies it can meet the contract

## Project Structure

```
src/test/java/contract/
├── consumer/
│   ├── UserServiceConsumerContractTest.java
│   └── PostServiceConsumerContractTest.java
└── provider/
    └── UserServiceProviderContractTest.java
```

## Consumer Contract Tests

Consumer tests define what the consumer expects from the provider.

### Example: User Service Consumer

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
            .body("{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"email\": \"john.doe@example.com\"\n" +
                "}")
        .toPact(V4Pact.class);
}
```

**Key Components:**
- **Consumer**: Service that calls the API
- **Provider**: Service that provides the API
- **Given**: Provider state (precondition)
- **Upon Receiving**: Request description
- **Will Respond With**: Expected response

## Provider Contract Tests

Provider tests verify that the provider can fulfill the contracts.

### Example: User Service Provider

```java
@Provider("UserProvider")
@PactFolder("target/pacts")
public class UserServiceProviderContractTest {
    
    @State("user with id 1 exists")
    public void userWithId1Exists() {
        // Setup: Ensure user with id 1 exists in test database
    }
}
```

**Key Components:**
- **@Provider**: Identifies the provider service
- **@PactFolder**: Location of pact files
- **@State**: Sets up provider state for the test

## Running Contract Tests

### Run Consumer Tests
```bash
# Run all consumer contract tests
mvn test -Dtest=contract.consumer.*

# Run specific consumer test
mvn test -Dtest=UserServiceConsumerContractTest
```

### Run Provider Tests
```bash
# Run all provider contract tests
mvn test -Dtest=contract.provider.*

# Run specific provider test
mvn test -Dtest=UserServiceProviderContractTest
```

### Generated Pact Files
After running consumer tests, pact files are generated in:
```
target/pacts/UserConsumer-UserProvider.json
target/pacts/PostConsumer-PostProvider.json
```

## Contract Test Examples

### 1. Get User by ID
**Consumer expects:**
- GET /users/1
- Status: 200
- Response contains: id, name, email

**Provider must:**
- Return user with id 1
- Include all required fields
- Return 200 status

### 2. Create User
**Consumer expects:**
- POST /users with user data
- Status: 201
- Response contains created user with id

**Provider must:**
- Accept POST request
- Create user
- Return 201 with created user

### 3. User Not Found
**Consumer expects:**
- GET /users/999
- Status: 404

**Provider must:**
- Return 404 for non-existent users

## Best Practices

### 1. Consumer-Driven Contracts
- Consumers define what they need
- Providers verify they can deliver
- Prevents over-specification

### 2. Provider States
```java
@State("user with id 1 exists")
public void setupUser() {
    // Create test data
    testDatabase.insertUser(1, "John Doe");
}
```

### 3. Meaningful Descriptions
```java
.uponReceiving("a request to get user by id")  // Good
.uponReceiving("test 1")                       // Bad
```

### 4. Version Your Contracts
- Tag pacts with version numbers
- Use Pact Broker for contract management
- Track breaking changes

### 5. Test Edge Cases
- Success scenarios (200, 201)
- Error scenarios (404, 400, 500)
- Validation failures
- Authentication failures

## Contract Testing Workflow

```
┌─────────────┐
│  Consumer   │
│   Team      │
└──────┬──────┘
       │ 1. Write consumer test
       │ 2. Generate pact file
       ▼
┌─────────────┐
│ Pact Broker │ (Optional)
│  (Storage)  │
└──────┬──────┘
       │ 3. Share contract
       ▼
┌─────────────┐
│  Provider   │
│   Team      │
└──────┬──────┘
       │ 4. Verify contract
       │ 5. Deploy if passing
       ▼
   ✅ Success
```

## Integration with CI/CD

### In Pull Requests
```yaml
- name: Run Contract Tests
  run: mvn test -Dtest=contract.*

- name: Publish Pacts
  run: mvn pact:publish
```

### Contract Verification
- Consumer tests run on consumer changes
- Provider tests run on provider changes
- Both verify the contract is maintained

## Common Scenarios

### Scenario 1: Adding a New Field
**Consumer needs new field:**
1. Update consumer test with new field
2. Generate new pact
3. Provider test fails (missing field)
4. Provider adds field
5. Provider test passes

### Scenario 2: Removing a Field
**Provider wants to remove field:**
1. Check if any consumer uses it (via pact broker)
2. If used, coordinate with consumer team
3. Consumer removes dependency
4. Provider can safely remove field

### Scenario 3: Changing Response Format
**Breaking change detected:**
1. Provider test fails
2. Identify affected consumers
3. Version the API or coordinate migration
4. Update contracts

## Troubleshooting

### Pact File Not Generated
- Check test execution logs
- Verify `target/pacts` directory exists
- Ensure consumer test runs successfully

### Provider Verification Fails
- Check provider state setup
- Verify API is running on correct port
- Check response format matches contract

### Port Conflicts
```java
@PactTestFor(pactMethod = "getUserByIdPact", port = "8080")
```
Use different ports for different services.

## Resources

- **Pact Documentation**: https://docs.pact.io/
- **Pact JVM**: https://github.com/pact-foundation/pact-jvm
- **Contract Testing Guide**: https://martinfowler.com/articles/consumerDrivenContracts.html

## Summary

Contract testing sits between unit tests and integration tests:
- **Faster** than integration tests
- **More realistic** than unit tests with mocks
- **Catches breaking changes** early
- **Enables independent deployment** of services

Use contract testing when:
- ✅ Building microservices
- ✅ Multiple teams own different services
- ✅ Need to deploy services independently
- ✅ Want to catch API breaking changes early
