
# Expense Tracker API

A RESTful backend application built with **Java 21 and Spring Boot** to manage personal income and expenses. The project focuses on building a production-style backend with clean architecture, database migrations, validation, pagination, and filtering.

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.1.1
- **Build Tool:** Maven
- **Database:** PostgreSQL (Supabase)
- **ORM:** Spring Data JPA, Hibernate
- **Database Migrations:** Flyway
- **API:** REST
- **Validation:** Jakarta Bean Validation

## Features

- Create income and expense transactions.
- Retrieve transactions with pagination.
- Sort transactions by supported fields.
- Filter transactions by type and date range.
- Validate incoming request data.
- Generate UUIDs for transaction IDs.
- Automatically track creation and modification timestamps.
- Manage database schema changes using Flyway migrations.
- Validate entity mappings against the database schema using Hibernate.

## Project Structure

```text
src/
├── main/
│   ├── java/com/expensetrack/expensetrack/
│   │   ├── common/
│   │   │   └── entity/
│   │   │       └── BaseEntity.java
│   │   ├── transaction/
│   │   │   ├── Transaction.java
│   │   │   ├── TransactionController.java
│   │   │   ├── TransactionRepository.java
│   │   │   ├── TransactionService.java
│   │   │   ├── TransactionSpecifications.java
│   │   │   ├── TransactionType.java
│   │   │   └── dto/
│   │   │       ├── CreateTransactionRequest.java
│   │   │       └── TransactionResponse.java
│   │   └── ExpensetrackApplication.java
│   └── resources/
│       ├── db/migration/
│       │   ├── V1__create_transactions.sql
│       │   └── V2__add_audit_timestamps.sql
│       └── application.properties
└── test/
```

*Note: Update the structure as additional packages and classes are implemented.*

## Getting Started

### Prerequisites

- Java 21 or later
- Git
- PostgreSQL database or a Supabase PostgreSQL project

### 1. Clone the repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd expensetrack
```

Navigate into the directory containing `pom.xml` if the project is nested.

### 2. Configure environment variables

The application reads database credentials from environment variables. Set these in your terminal before starting the application.

**Linux / Pop!_OS**

```bash
export DB_URL="jdbc:postgresql://<HOST>:5432/<DATABASE>"
export DB_USERNAME="<DATABASE_USERNAME>"
export DB_PASSWORD="<DATABASE_PASSWORD>"
```

**Windows PowerShell**

```powershell
$env:DB_URL = "jdbc:postgresql://<HOST>:5432/<DATABASE>"
$env:DB_USERNAME = "<DATABASE_USERNAME>"
$env:DB_PASSWORD = "<DATABASE_PASSWORD>"
```

Replace the placeholders with your actual PostgreSQL connection details. For Supabase, use the JDBC-compatible connection string and credentials provided in its dashboard.

**Security:** Never commit database credentials, API keys, or other secrets to GitHub.

### 3. Run the application

**Linux / Pop!_OS**

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

**Windows PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

Flyway runs database migrations on application startup.

## API Endpoints

Base URL: `http://localhost:8080`

### Create a transaction

`POST /api/v1/transactions`

Request body:

```json
{
  "title": "Grocery shopping",
  "amount": 1250.50,
  "transactionType": "EXPENSE",
  "date": "2026-10-20"
}
```

Supported transaction types:

- `INCOME`
- `EXPENSE`

### Get transactions

`GET /api/v1/transactions`

Retrieve transactions with pagination, sorting, and optional filters.

**Example:**

```http
GET /api/v1/transactions?type=EXPENSE&from=2026-10-01&to=2026-10-31&page=0&size=10&sort=date,desc
```

Query parameters:

| Parameter | Description |
|---|---|
| `type` | Filter by `INCOME` or `EXPENSE` |
| `from` | Include transactions from this date |
| `to` | Include transactions up to this date |
| `page` | Zero-based page number |
| `size` | Number of records per page |
| `sort` | Sorting field and direction, e.g. `date,desc` |

## Database Design

### Transactions

| Column | Description |
|---|---|
| `id` | UUID primary key |
| `title` | Transaction description |
| `amount` | Positive monetary amount |
| `transaction_type` | `INCOME` or `EXPENSE` |
| `transaction_date` | Date of the transaction |
| `created_at` | Record creation timestamp |
| `updated_at` | Last modification timestamp |

Database schema changes are version-controlled through Flyway migration scripts.

## Engineering Practices

- Layered architecture with controllers, services, repositories, and DTOs.
- Constructor-based dependency injection.
- Request validation.
- `BigDecimal` for monetary values.
- UUID identifiers.
- Centralized timestamp auditing.
- Database schema migrations.
- Pagination, sorting, and dynamic filtering.
- Environment-based configuration.

## Roadmap

- [ ] User registration and login
- [ ] JWT-based authentication and authorization
- [ ] Associate transactions with individual users
- [ ] Update and delete transactions
- [ ] Categories and budgets
- [ ] Monthly income and expense summaries
- [ ] Global exception handling
- [ ] Unit and integration tests
- [ ] API documentation with OpenAPI / Swagger
- [ ] Docker support
- [ ] RabbitMQ and Kafka integration for event-driven processing

## Learning Goals

This project is being developed incrementally to explore Spring Boot backend development, REST API design, persistence, authentication, testing, and distributed-system concepts.

---

**Status:** In development.
