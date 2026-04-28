# Automation Project

Selenium WebDriver test automation framework for web application testing.

## 🚀 Features

- Selenium WebDriver 4.x
- TestNG framework
- Page Object Model (POM) design pattern
- Automated CI/CD with GitHub Actions
- Comprehensive test reporting

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6+
- Chrome/Firefox browser
- Git

## 🛠️ Setup

1. Clone the repository:
```bash
git clone https://github.com/Gomathy1/AutomationProject.git
cd AutomationProject
```

2. Install dependencies:
```bash
mvn clean install
```

3. Run tests:
```bash
mvn test
```

## 📁 Project Structure

```
AutomationProject/
├── .github/
│   ├── workflows/          # CI/CD workflows
│   └── CODEOWNERS         # Code review assignments
├── src/
│   ├── main/java/
│   │   ├── pages/         # Page Object classes
│   │   └── utils/         # Utility classes
│   └── test/java/
│       └── tests/         # Test classes
├── pom.xml                # Maven dependencies
└── README.md
```

## 🧪 Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=LoginTest

# Run with specific browser
mvn test -Dbrowser=chrome
```

## 📊 Test Reports

Test reports are generated in `target/surefire-reports/`

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Submit a pull request
4. Wait for review and approval

## 📝 License

MIT License
