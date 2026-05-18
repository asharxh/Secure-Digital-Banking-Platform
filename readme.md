# Secure Digital Banking Platform

A production-style backend banking system built using modern Java and Spring Boot technologies to simulate real-world digital banking operations.

This project focuses on building a secure and scalable banking backend with features such as:

* User authentication with JWT
* Role-Based Access Control (RBAC)
* Bank account management
* Deposits, withdrawals, and transfers
* Transaction history and statements
* Fraud detection system
* Audit logging
* Account freeze/unfreeze system
* Exception handling and validation

The goal of this project is to deeply understand backend architecture, financial transaction systems, database design, and secure API development.

---

# Tech Stack

* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Lombok

---

# Features

## Authentication & Security

* JWT-based authentication
* Password encryption using BCrypt
* Role-Based Access Control (USER / ADMIN)
* Secure protected APIs
* Account ownership validation
* Unauthorized access prevention

---

## Banking Features

* User registration & login
* Create bank account
* Multiple accounts per user
* Deposit money
* Withdraw money
* Transfer money between accounts
* Account statement with date filtering
* Transaction history tracking

---

## Admin Features

* View all users
* View all accounts
* View all transactions
* View audit logs
* View fraud alerts
* Freeze bank accounts
* Unfreeze bank accounts

---

## Fraud Detection System

The system automatically detects suspicious activities such as:

* Large deposits
* Large withdrawals
* Large transfers

Fraud alerts are stored in the database and visible to admins.

---

## Audit Logging System

Tracks important system activities:

* Deposits
* Withdrawals
* Transfers
* Admin actions
* Security-sensitive operations

Each audit log contains:

* Username
* Action
* Details
* Timestamp

---

## Account Freeze System

Admins can freeze/unfreeze bank accounts.

Frozen accounts cannot:

* Deposit money
* Withdraw money
* Transfer money

---

## Pagination Support

Pagination implemented for:

* Users
* Accounts
* Transactions

Supports:

* page
* size
* sorting

---

# Project Structure

```text
com.ashar.securedigitalbankingplatform
│
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── exception
```

---

# Database Configuration

## Create PostgreSQL Database

```sql
CREATE DATABASE bankdb;
```

---

## application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bankdb
spring.datasource.username=ashar
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

# Database Design

## User Entity

| Field     | Type          |
| --------- | ------------- |
| id        | Long          |
| name      | String        |
| email     | String        |
| password  | String        |
| role      | USER / ADMIN  |
| createdAt | LocalDateTime |
| updatedAt | LocalDateTime |

---

## BankAccount Entity

| Field         | Type              |
| ------------- | ----------------- |
| id            | Long              |
| accountNumber | String            |
| balance       | Double            |
| accountType   | SAVINGS / CURRENT |
| frozen        | boolean           |
| user_id       | FK                |
| createdAt     | LocalDateTime     |
| updatedAt     | LocalDateTime     |

---

## Transaction Entity

| Field           | Type          |
| --------------- | ------------- |
| id              | Long          |
| referenceNumber | String        |
| type            | String        |
| amount          | Double        |
| senderAccount   | String        |
| receiverAccount | String        |
| description     | String        |
| timestamp       | LocalDateTime |
| account_id      | FK            |

---

## AuditLog Entity

| Field     | Type          |
| --------- | ------------- |
| id        | Long          |
| username  | String        |
| action    | String        |
| details   | String        |
| timestamp | LocalDateTime |

---

## FraudAlert Entity

| Field         | Type          |
| ------------- | ------------- |
| id            | Long          |
| accountNumber | String        |
| reason        | String        |
| amount        | Double        |
| timestamp     | LocalDateTime |

---

# API Endpoints

# Authentication APIs

## Register User

```http
POST /users/register
```

### Request Body

```json
{
  "name": "Ashar",
  "email": "ashar@gmail.com",
  "password": "123456"
}
```

---

## Login

```http
POST /users/login
```

### Request Body

```json
{
  "email": "ashar@gmail.com",
  "password": "123456"
}
```

### Response

```json
{
  "token": "jwt_token_here",
  "message": "Login successful"
}
```

---

# Account APIs

## Create Account

```http
POST /accounts/create
```

### Headers

```text
Authorization: Bearer your_jwt_token
```

### Request Body

```json
{
  "accountType": "SAVINGS"
}
```

---

## Get My Accounts

```http
GET /accounts/my
```

---

## Deposit Money

```http
POST /accounts/deposit
```

### Params

```text
accountNumber=ACC123
amount=500
```

---

## Withdraw Money

```http
POST /accounts/withdraw
```

---

## Transfer Money

```http
POST /accounts/transfer
```

### Params

```text
fromAccount=ACC123
toAccount=ACC456
amount=1000
```

---

# Transaction APIs

## Transaction History

```http
GET /accounts/transactions
```

---

## Account Statement

```http
GET /accounts/statement
```

### Params

```text
accountNumber=ACC123
startDate=2026-01-01
endDate=2026-12-31
```

---

# Admin APIs

## Get All Users

```http
GET /admin/users?page=0&size=5
```

---

## Get All Accounts

```http
GET /admin/accounts?page=0&size=5
```

---

## Get All Transactions

```http
GET /admin/transactions?page=0&size=5
```

---

## Get Audit Logs

```http
GET /admin/audit-logs
```

---

## Get Fraud Alerts

```http
GET /admin/fraud-alerts
```

---

## Freeze Account

```http
POST /admin/accounts/{accountNumber}/freeze
```

---

## Unfreeze Account

```http
POST /admin/accounts/{accountNumber}/unfreeze
```
---

# Security Architecture

The system uses:

* JWT Authentication Filter
* Spring Security Filter Chain
* Role-based authorization
* Secure password hashing
* Ownership validation

---

# Future Improvements

Planned upgrades:


* Redis caching
* Unit & integration testing
* Rate limiting
* Microservices architecture
* AI Customer Support Bot
* AI Fraud Detection
* Kafka for Transaction Events
* Security Enhancements

---

# Project Goal

The goal of this project is to simulate a real-world secure digital banking backend while learning industry-standard backend development practices, scalable architecture, and secure financial transaction processing.

---

# Author

**Ashar**

GitHub: https://github.com/asharxh

LinkedIn: https://www.linkedin.com/in/ashararif/
