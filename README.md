# restful-api-qa-items

This project contains automated API tests for item management using **Java**, **Cucumber**, and **JUnit 5**. It includes hooks to print scenario execution details in real time.

## Project Structure

restful-api-qa-items/
├── src/
│ ├── main/
│ │ └── java/
│ │ └── com/
│ │ └── api/
│ │ ├── model/ ← POJOs (e.g., ItemData.java, ItemRequest.java)
│ │ └── utils/ ← Utilities and context classes (e.g., ApiTestContext.java, ConfigReader.java)
│ └── test/
│ ├── java/
│ │ └── com/
│ │ └── api/
│ │ ├── steps/ ← Cucumber step definitions (e.g., ItemManagementSteps.java)
│ │ ├── runners/ ← Test runners (e.g., RunCucumberTest.java)
│ │ └── hooks/ ← Cucumber hooks (e.g., Hooks.java)
│ └── resources/
│ └── features/ ← Feature files (e.g., item_management.feature)
├── pom.xml ← Maven configuration
└── README.md ← Project documentation

# Prerequisites

- **Java 21** 
- **Maven 3.8+**
- **IntelliJ IDEA** (or any IDE that supports Maven and JUnit 5)

# Key Dependencies

- **Cucumber Java 7+** – for BDD-style feature testing
- **JUnit 5 / JUnit Platform** – test runner
- **Rest-Assured** – for REST API testing
- **Jackson Databind** – for JSON serialization
- **Cucumber PicoContainer** – for dependency injection in step definitions

Dependencies are managed in `pom.xml`.


# Running Tests

# Run all tests
mvn clean test

# Run a specific feature
mvn clean test -Dcucumber.features=src/test/resources/features/item_management.feature

# Run Maven and override base.url
mvn clean test -Dbase.url=https://staging.restful-api.dev/objects


# Hooks
The project includes Before and After hooks to log scenario execution.

# Before Hook
Prints the scenario name before it starts:

# After Hook
Prints the scenario name and status after it finishes:

# Features
Item Management – create, read, update, delete items via REST API

Notes
Step definitions are located in src/test/java/com/api/steps/.
Hooks are located in src/test/java/com/api/hooks/.
Feature files are located in src/test/resources/features/.
Runner class is located in src/test/java/com/api/runners/RunCucumberTest.java.
Dependency Injection – using PicoContainer for step class dependencies
Maven Surefire plugin automatically detects the runner based on the *Test.java naming convention.

Hooks provide real-time logging and status tracking for all scenarios.
Ensure your JSON serialization library (e.g., Jackson) is on the classpath; otherwise Rest-Assured will fail to serialize objects.
Keep the features folder separate from target/ to avoid duplicate feature warnings when running tests.