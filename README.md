# Rest Assured API Test Demo

This project contains automated API tests for item management using **Java**, **Cucumber**,**RestAssured** and **JUnit 5**. It includes hooks to print scenario execution details in real time.

## Badges

![CI](https://github.com/Sand5/restful-api-qa-items/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/java-21-blue)

---

## Test Reports

After running the CI workflow, test reports are available as artifacts:

- **Surefire Reports:** `target/surefire-reports`
- **Cucumber Reports:** `target/cucumber-report.html`

You can download them from the **Actions → build → Artifacts** section on GitHub.

## Project Structure
```

restful-api-qa-items/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── api/
│   │               ├── model/      ← POJOs (e.g., ItemData.java, ItemRequest.java)
│   │               └── utils/      ← Utilities (e.g., ConfigReader.java)
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── api/
│       │           ├── steps/      ← Cucumber step definitions (e.g., ItemManagementSteps.java, Hooks.java)
│       │           ├── runners/    ← Test runners (e.g., RunCucumberTest.java)
│       │           └── utils/      ← Test context (e.g., ApiTestContext.java)
│       └── resources/
│           ├── config.properties   ← API configuration (base URL, API key)
│           ├── features/           ← Feature files (e.g., item_management.feature)
│           └── junit-platform.properties
├── target/                         ← Build output
│   ├── classes/                    ← Compiled main classes
│   ├── test-classes/               ← Compiled test classes
│   ├── cucumber-report.html        ← HTML test report
│   ├── cucumber-report.json        ← JSON test report
│   ├── surefire-reports/           ← Surefire test run results (XML + TXT)
│   └── generated-sources/          ← Generated source files
├── pom.xml                          ← Maven configuration
└── README.md                        ← Project documentation
```
## Prerequisites

- **Java 21** 
- **Maven 3.8+**
- **IntelliJ IDEA** (or any IDE that supports Maven and JUnit 5)

## Key Dependencies

- **Cucumber Java 7+** – for BDD-style feature testing
- **JUnit 5 / JUnit Platform** – test runner
- **Rest-Assured** – for REST API testing
- **Jackson Databind** – for JSON serialization
- **Cucumber PicoContainer** – for dependency injection in step definitions

Dependencies are managed in `pom.xml`.

## Run all tests
mvn clean test

## Run a specific feature
mvn clean test -Dcucumber.features=src/test/resources/features/item_management.feature

## Run Maven and override base.url
mvn clean test -Dbase.url=https://staging.restful-api.dev/objects

## Overriding Configuration
By default, this framework reads values from src/test/resources/config.properties (e.g., base.url, api.key).
You can override these values for different environments using system properties or environment variables.

### Override Locally with Maven

Use system properties with -D:

mvn clean test -Dbase.url="https://staging.api.example.com" -Dapi.key="12345abcdef"
base.url → overrides the URL from config.properties
api.key → overrides the API key from config.properties

The tests will use these values for the current run only.

### Override Using Environment Variables

The framework also supports environment variables. The mapping converts property names to uppercase and replaces dots with underscores:

export BASE_URL="https://staging.api.example.com"

export API_KEY="12345abcdef"

mvn clean test

##  Running Tests with Docker

This project supports running API tests inside a Docker container, removing the need to install Java or Maven locally.

### Prerequisites
Docker installed (Docker Desktop or Docker Engine)

### Build the Docker Image

From the root of the project (where the Dockerfile is located), run:

docker build -t rest-api-tests .

### Run All Tests
docker run --rm rest-api-tests

This executes:
mvn clean test inside the container.

### Run a Specific Feature

To run a single Cucumber feature:
docker run --rm rest-api-tests \
mvn clean test -Dcucumber.features=src/test/resources/features/item_management.feature

###  Override Configs in Docker
docker run --rm \
-e BASE_URL="https://staging.api.example.com" \
-e API_KEY="12345abcdef" \
rest-api-tests

Environment variables take priority over system properties and config.properties.
This is ideal for CI/CD pipelines or running tests in different environments without changing code.
### Export Test Reports

By default, test reports are generated inside the container. To save them locally, mount a volume:
docker run --rm \
-v $(pwd)/reports:/app/target \
rest-api-tests

After execution, reports will be available in ./reports/ and include:

cucumber-report.html
cucumber-report.json
surefire-reports/

### Notes
The Docker image uses Maven 3.9 and Java 21 (Eclipse Temurin)
Tests run in an isolated environment for consistency across machines and CI pipelines
No local Java/Maven installation is required

### Troubleshooting Docker
Dockerfile not found: Ensure the file is named exactly Dockerfile (case-sensitive)
No tests executed: Verify src/test is correctly copied into the container
Dependency issues: Rebuild the image without cache:
docker build --no-cache -t rest-api-tests .

### Test Hooks
The project includes Before and After hooks to log scenario execution.

### Before Hook
Prints the scenario name before it starts:

### After Hook
Prints the scenario name and status after it finishes:

### Features
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