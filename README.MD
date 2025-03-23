# Loan Management System (LMS)

## Overview
The Loan Management System (LMS) is a Spring Boot-based application designed to manage loan subscriptions and requests for customers. It provides a RESTful API to handle customer subscriptions, loan applications, and basic data persistence using PostgreSQL. This project was initially developed with the intent to integrate with an external Scoring Engine API and a SOAP-based Core Banking System (CBS). Due to constraints (detailed below), this version focuses on core functionality without those integrations.

## Key Features
- **Customer Subscription:** Allows customers to subscribe via a REST endpoint (`/api/v1/subscribe`).
- **Loan Processing:** Basic loan request handling with mock responses (`/api/v1/loan/request`).
- **Data Persistence:** Stores customer and loan data in a PostgreSQL database.
- **Security:** Includes Spring Security for basic authentication (configurable).
- **Deployment:** Runs using Docker Compose for containerized deployment.

## Current Status
- **Date:** March 23, 2025
- **Version:** 0.0.1-SNAPSHOT
- **State:** Reverted to a minimal working version due to compilation issues with SOAP (JAX-WS/JAXB) and Scoring Engine integration attempts.

## Project Structure
```
LoanManagementSystem/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/credable/lms/
│   │   │       ├── controller/    # REST controllers
│   │   │       ├── entity/        # JPA entities (e.g., Customer, Loan)
│   │   │       ├── repository/    # JPA repositories
│   │   │       ├── service/       # Business logic (e.g., LoanService)
│   │   │       ├── App.java       # Main application class
│   │   │       └── LmsApplication.java # Spring Boot entry point
│   │   └── resources/
│   │       ├── application.yml    # Configuration
│   │       └── keystore.p12       # SSL keystore (if applicable)
│   └── test/                      # Unit tests
├── Dockerfile                     # Docker build instructions
├── docker-compose.yml             # Docker Compose configuration
├── pom.xml                        # Maven dependencies and build config
└── README.md                      # This file
```

## Prerequisites
- **Java:** 17
- **Maven:** 3.9.6
- **Docker:** Latest version
- **Docker Compose:** Latest version
- **PostgreSQL:** Installed and configured

## Setup and Deployment

### Local Development
#### 1. Clone the Repository
```bash
git clone <repository-url>
cd LoanManagementSystem
```
#### 2. Build the Project
```bash
mvn clean install
```
#### 3. Run Locally
Ensure PostgreSQL is running (database: `lms_db`, user: `postgres`, password: `<your_secure_password>`).

Update `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/lms_db
    username: postgres
    password: your_secure_password
```
Start the application:
```bash
mvn spring-boot:run
```

### Docker Deployment
#### 1. Update `docker-compose.yml`
```yaml
version: '3.8'
services:
  lms-app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/lms_db
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=your_secure_password
    depends_on:
      - db
    networks:
      - lms-network
  db:
    image: postgres:14
    environment:
      - POSTGRES_DB=lms_db
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=your_secure_password
    volumes:
      - db-data:/var/lib/postgresql/data
    networks:
      - lms-network
networks:
  lms-network:
    driver: bridge
volumes:
  db-data:
```
Replace `your_secure_password` with a secure value.

#### 2. Build and Run
```bash
docker-compose up -d --build
```
#### 3. Verify
Check running containers:
```bash
docker ps
```
View logs:
```bash
docker-compose logs lms-app
```

## API Endpoints

### 1. Subscribe Customer
- **URL:** `GET /api/v1/subscribe?customerNumber={customerNumber}`
- **Description:** Subscribes a customer to the LMS.
- **Response:**
```json
{
  "message": "Customer 234774784 subscribed successfully"
}
```

### 2. Request Loan
- **URL:** `GET /api/v1/loan/request?customerNumber={customerNumber}&amount={amount}`
- **Description:** Processes a loan request (mock response).
- **Response:**
```json
{
  "message": "Loan processed for customer 234774784 with amount 5000"
}
```

## Constraints

### 1. Scoring Engine Integration
- **Issue:** The external Scoring Engine API was unavailable during development.
- **Impact:** No real-time credit scoring.
- **Workaround:** Mock responses implemented in `LoanService`.

### 2. SOAP Integration (CBS)
- **Issue:** Java 17 compatibility issues with `javax.xml.ws` and `javax.xml.bind`.
- **Impact:** No integration with the Core Banking System.
- **Workaround:** Removed SOAP-related code to ensure build success.

### 3. Limited Functionality
- **Issue:** Minimal state, lacking advanced features like transaction processing.
- **Impact:** Only basic subscription and loan request endpoints are functional.

### 4. Security
- **Issue:** SSL/TLS configuration is present but not fully implemented.
- **Impact:** HTTP is used instead of HTTPS in testing.

### 5. Error Handling
- **Issue:** Basic error handling; no retries or fallback mechanisms.
- **Impact:** App may crash on unexpected errors.

## Improvements Needed

### 1. Scoring Engine Integration
- **Action:** Reintroduce `ScoringRestClient` once the API is available.
- **Steps:** Implement retry logic using Spring Retry.

### 2. SOAP Integration
- **Action:** Restore CBS integration with JAX-WS/JAXB dependencies.
- **Steps:** Add required dependencies to `pom.xml` and regenerate SOAP classes.

### 3. Enhanced Security
- **Action:** Enable HTTPS with `keystore.p12`.
- **Steps:** Update `application.yml` to configure SSL.

### 4. Robust Error Handling
- **Action:** Add global exception handling and retries.
- **Steps:** Implement `@ControllerAdvice` and integrate Spring Retry.

### 5. Testing
- **Action:** Add unit and integration tests.
- **Steps:** Expand `src/test/java` with JUnit tests.

### 6. Documentation
- **Action:** Enhance API docs with OpenAPI/Swagger.
- **Steps:** Add `springdoc-openapi-starter-webmvc-ui` dependency.

### 7. Performance
- **Action:** Optimize database queries and add caching.
- **Steps:** Use `@Cacheable` for frequent queries.

## Notes
- **Reason for Reversion:** SOAP and Scoring Engine integrations were attempted but reverted due to API unavailability and Java 17 compatibility issues.
- **Next Steps:** Post-submission, resolve external API dependencies and enhance features as outlined above.


