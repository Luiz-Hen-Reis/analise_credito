# Credit Analysis API - v1.0.0

![Badge](https://img.shields.io/badge/Java_21-Spring%20Boot%204.0-success)
![Badge](https://img.shields.io/badge/OAS-3.1-blue)
![Badge](https://img.shields.io/badge/H2-In--Memory%20Database-informational)
![Badge](https://img.shields.io/badge/Unit%20Tests-JUnit%20%7C%20Mockito-yellowgreen)
![Badge](https://img.shields.io/badge/Design%20Pattern-Strategy-orange)

REST API for automated credit analysis, applying a rule engine based on the **Strategy Pattern** to evaluate customer eligibility, financial capacity, credit behavior, and loan object validation.

## 📌 Overview

- **Technologies**: Java 21, Spring Boot 4.0, Spring Data JPA, H2 Database, Lombok, Bean Validation
- **Documentation**: [OpenAPI 3.1](http://localhost:8080/v3/api-docs) | [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- **Server**: `http://localhost:8080` (Development)
- **Unit Tests**: Tested with JUnit 5 and Mockito covering all use cases and credit rules individually

## 🚀 Endpoints

### 👤 Customers

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customers` | List all customers (paginated) |
| POST | `/api/customers` | Create a new customer |
| GET | `/api/customers/{id}` | Find customer by ID |
| GET | `/api/customers/cpf/{cpf}` | Find customer by CPF |
| PATCH | `/api/customers/{id}` | Update a customer |
| DELETE | `/api/customers/{id}` | Delete a customer |

### 📊 Credit Analysis

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/credit-analysis` | Run credit analysis for a customer |

## 🧠 Credit Rule Engine

The engine is built with the **Strategy Pattern** — each rule is an independent component evaluated in order. The engine **stops at the first failing rule** (short-circuit), returning the reason for denial.

### Rule Execution Order

| Order | Rule | Category | Description |
|-------|------|----------|-------------|
| 1 | `AgeRule` | Eligibility | Customer must be between 18 and 75 years old |
| 2 | `BlacklistRule` | Eligibility | Simulates a blacklist query (20% chance of denial) |
| 3 | `MinimumIncomeRule` | Financial | Monthly income must be at least R$ 2,000.00 |
| 4 | `IncomeCommitmentRule` | Financial | Installment cannot exceed 30% of monthly income |
| 5 | `CreditScoreRule` | Behavior | Credit score must be at least 600 |
| 6 | `RecentQueriesRule` | Behavior | Maximum of 5 credit queries in the last 30 days |
| 7 | `InstallmentAgeRule` | Loan Object | Customer age at end of contract cannot exceed 80 years |

### Score Calculation (on approval)

The base score is the customer's provided credit score, with the following bonuses applied:

- **+20 points** — zero recent queries
- **+30 points** — monthly income above R$ 5,000.00
- **Max score capped at 1000**

## 🗂️ Project Structure

```
src/main/java/com/henr/analise_credito/
├── config/                  # Swagger configuration
├── controller/              # REST controllers
├── credit/
│   ├── engine/              # CreditEngine (rule orchestrator)
│   ├── model/               # CreditRequest, AnalysisResult
│   └── rule/
│       ├── eligibility/     # AgeRule, BlacklistRule
│       ├── financial/       # MinimumIncomeRule, IncomeCommitmentRule
│       ├── behavior/        # CreditScoreRule, RecentQueriesRule
│       └── object/          # InstallmentAgeRule
├── dto/                     # Request/Response DTOs
├── entity/                  # JPA entities (Customer, CreditAnalysis)
├── exception/               # Global exception handler
├── repository/              # Spring Data repositories
└── useCase/                 # Business logic use cases
```

## 📖 API Documentation (Swagger)

The API is fully documented with OpenAPI 3.1 via **SpringDoc**. After starting the application, access:

| URL | Description |
|-----|-------------|
| `http://localhost:8080/swagger-ui/index.html` | Interactive Swagger UI |
| `http://localhost:8080/v3/api-docs` | Raw OpenAPI JSON spec |

### What's documented

- **Request body schemas** with field descriptions and examples
- **Response schemas** for each status code (200, 201, 400, 404, 409)
- **Error response format** (`ErrorResponse`) shown for all failure cases
- **Grouped by tag**: `Customers` and `Credit Analysis`

### Error Response Format

All errors follow a consistent structure:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found",
  "path": "/api/customers/uuid",
  "timestamp": "2026-02-26T19:00:00",
  "fields": null
}
```

For validation errors (`400`), the `fields` array is populated:

```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Invalid fields",
  "path": "/api/customers",
  "timestamp": "2026-02-26T19:00:00",
  "fields": [
    { "field": "cpf", "message": "CPF is required" },
    { "field": "birthDate", "message": "Birth date must be a date in the past" }
  ]
}
```

## 🛠️ Configuration

### Prerequisites

- Java 21+
- Maven 3.8+

### Running the Application

```bash
mvn spring-boot:run
```

### H2 Console (in-memory database)

Access the database console at:

```
http://localhost:8080/h2-console
```

| Field | Value |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:analise_credito` |
| Username | `sa` |
| Password | _(empty)_ |

## ✅ Running Tests

```bash
mvn test
```

### Test Coverage

| Class | Tests |
|-------|-------|
| `CreateCustomerUseCase` | Customer already exists, successful creation |
| `DeleteCustomerUseCase` | Customer not found, successful deletion |
| `UpdateCustomerUseCase` | Not found, partial update, full update |
| `ListAllCustomersUseCase` | Empty list, mapped customers |
| `ListCustomerByCpfUseCase` | Not found, found by CPF |
| `ListCustomerByIdUseCase` | Not found, found by ID |
| `RunCreditAnalysisUseCase` | Customer not found, approved analysis, denied analysis |
| `CreditEngine` | Rule short-circuit, score bonuses, score cap |
| `AgeRule` | Min/max age boundaries, within range, outside range |
| `BlacklistRule` | Controlled randomness via injected `Random` |
| `MinimumIncomeRule` | Exact minimum, above, below, zero |
| `IncomeCommitmentRule` | Below 30%, exactly 30%, exceeds 30% |
| `CreditScoreRule` | Exact minimum, above, below, zero |
| `RecentQueriesRule` | Zero, exactly max, below, exceeds |
| `InstallmentAgeRule` | Within range, exactly 80, exceeds 80 |

## 📬 Example Request

### Create Customer

```json
POST /api/customers
{
  "name": "John Doe",
  "cpf": "12345678901",
  "birthDate": "1990-05-20"
}
```

### Run Credit Analysis

```json
POST /api/credit-analysis
{
  "cpf": "12345678901",
  "monthlyIncome": 5000.00,
  "requestedAmount": 20000.00,
  "installments": 24,
  "creditScore": 700,
  "recentQueries": 2
}
```

### Example Response (Approved)

```json
{
  "id": "uuid",
  "customerName": "John Doe",
  "cpf": "12345678901",
  "approved": true,
  "calculatedScore": 730,
  "reason": "All rules passed successfully",
  "processedAt": "2026-02-26T19:00:00"
}
```

### Example Response (Denied)

```json
{
  "id": "uuid",
  "customerName": "John Doe",
  "cpf": "12345678901",
  "approved": false,
  "calculatedScore": 0,
  "reason": "Installment exceeds 30% of monthly income.",
  "processedAt": "2026-02-26T19:00:00"
}
```

## 👨‍💻 Author

**Luiz Henrique** — lhcontato2020@gmail.com
