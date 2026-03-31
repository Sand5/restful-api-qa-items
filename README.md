# RESTful API Test Automation Project

Designed and implemented a robust, maintainable API test automation framework in **Java** using **Cucumber**, **RestAssured**,**Junit 5** and **Maven**. The framework features a layered architecture with builder, endpoints, service, and steps layers, promoting separation of concerns and reusability. Implemented context management with ApiTestContext to isolate scenarios, along with Hooks for lifecycle handling, safe cleanup, and detailed logging using SLF4J. Test coverage includes CRUD operations, multiple scenarios per feature, and assertions for both positive and negative outcomes. Integrated data-driven testing with Cucumber DataTables and ensured resilience with safe delete and error handling. The project is CI-ready, containerized with Docker, and structured for scalability to other API endpoints.API service can be located at https://restful-api.dev/
## Badges

![CI](https://github.com/Sand5/restful-api-qa-items/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/java-21-blue)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white)
![Cucumber](https://img.shields.io/badge/Cucumber-BDD-23D96C?logo=cucumber&logoColor=white)
![API Testing](https://img.shields.io/badge/API-Testing-orange)
![Docker](https://img.shields.io/badge/docker-ready-2496ed?logo=docker&logoColor=white)
![Parallel Tests](https://img.shields.io/badge/Tests-Parallel-green)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-CI-2088FF?logo=githubactions&logoColor=white)
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
│   │               └── utils/       ← Main utilities / helpers
│   │                   └── ConfigReader.java
│   │               └── model/       ← Could also be shared DTOs if needed in main
│   │                   ├── ItemData.java
│   │                   └── ItemRequest.java
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── api/
│       │           ├── builder/     ← Test builders for requests
│       │           │   └── ItemRequestBuilder.java
│       │           ├── client/      ← Optional low-level HTTP wrappers
│       │           ├── endpoints/   ← API classes (ItemApi)
│       │           │   └── ItemApi.java
│       │           ├── service/     ← Service layer wrapping API calls
│       │           │   └── ItemService.java
│       │           ├── steps/       ← Cucumber step definitions
│       │           │   └── ItemManagementSteps.java
│       │           ├── hooks/       ← Lifecycle hooks (@Before, @After)
│       │           │   └── Hooks.java
│       │           ├── runners/     ← Test runners for Cucumber
│       │           │   └── RunCucumberTest.java
│       │           └── utils/       ← Test helpers / context
│       │               └── ApiTestContext.java
│       └── resources/
│           ├── config.properties
│           ├── features/
│           │   └── item_management.feature
│           └── junit-platform.properties
├── target/
│   ├── classes/
│   ├── test-classes/
│   ├── cucumber-report.html
│   ├── cucumber-report.json
│   ├── surefire-reports/
│   └── generated-sources/
├── .github/
│   └── workflows/
│       └── ci.yml
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── pom.xml
└── README.md
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

### Parallel Test Execution with Docker Compose using Tags

We use Docker Compose to demonstrate **parallel execution of Cucumber scenarios filtered by tags**.

### Running the Demo with Docker Compose 

1. Build and start containers:
docker-compose up --build

### Override threads or tags at runtime with Docker Compose:

### Key Commands:

| Purpose | Command |
|---------|---------|
| Run default Compose setup | `docker-compose up --build` |
| Override thread count | `THREAD_COUNT=4 docker-compose up --build` |
| Override tag | `CUCUMBER_TAGS=@regression docker-compose up --build` |
| Override both | `THREAD_COUNT=3 CUCUMBER_TAGS=@smoke docker-compose up --build` |
| View logs of a container | `docker logs restful-api-qa-items-smoke -f` |

Benefits

- Faster test execution

- Scenarios run concurrently in threads and containers

- Reduces total runtime for larger test suites

- Environment isolation

- Each container is self-contained
- Prevents conflicts from shared test state or APIs

- Flexible configuration

- Thread count and tags configurable per container via environment variables

- No changes needed in pom.xml for temporary overrides

- Reproducibility and consistency Docker ensures consistent environments across machines

## Note for Windows Users
```text
If you are using **Windows-native containers**, `sh -c` will **not work** because `sh` is not available. In that case use **CMD** or **PowerShell** for command execution.
- Example using CMD syntax: cmd /S /C "mvn clean test -Dcucumber.filter.tags=%CUCUMBER_TAGS% -Dparallel=scenarios -DthreadCount=%THREAD_COUNT% -DperCoreThreadCount=false"
- Example using PowerShell syntax: powershell -Command "mvn clean test -Dcucumber.filter.tags=$$env:CUCUMBER_TAGS -Dparallel=scenarios -DthreadCount=$env:THREAD_COUNT -DperCoreThreadCount=false"

or example override:
PowerShell example:$env:THREAD_COUNT=4
$env:CUCUMBER_TAGS="@smoke"
docker-compose up --build

CMD example override:
set THREAD_COUNT=4
set CUCUMBER_TAGS=@smoke
docker-compose up --build
```

Environment variables take priority over system properties and config.properties.
This is ideal for CI/CD pipelines or running tests in different environments without changing code.

### Export Test Reports from Dockerfile run

By default, test reports are generated inside the container. To save them locally, mount a volume:
docker run --rm \
-v $(pwd)/reports:/app/target \
rest-api-tests

After execution, reports will be available in ./reports/ and include:

cucumber-report.html
cucumber-report.json
surefire-reports/

### Export Test Reports from Docker Compose run
After the tests complete, the reports are inside the containers. Copy them to your local reports folder

#### Smoke reports
docker cp restful-api-qa-items-smoke:/app/target/cucumber-report.html ./reports/cucumber-report-smoke.html
docker cp restful-api-qa-items-smoke:/app/target/cucumber-report.json ./reports/cucumber-report-smoke.json

#### Regression reports
docker cp restful-api-qa-items-regression:/app/target/cucumber-report.html ./reports/cucumber-report-regression.html
docker cp restful-api-qa-items-regression:/app/target/cucumber-report.json ./reports/cucumber-report-regression.json

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