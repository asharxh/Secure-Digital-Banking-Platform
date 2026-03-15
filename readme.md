# Secure Digital Banking Platform

A backend banking system built using modern Java technologies to simulate real-world digital banking operations.

This project is being built step-by-step to understand how real banking systems work — including **user management, bank accounts, deposits, withdrawals, transfers, and transaction history**.

The goal is to practice **backend architecture, database design, and financial transaction logic** while building a production-style API.

---

# Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Lombok

---

# Project Structure

```
com.example.bank
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 ├── exception
```

# Database Configuration

```sql
CREATE DATABASE bankdb;
```

### application.properties

```
spring.datasource.url=jdbc:postgresql://localhost:5432/bankdb
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Spring Boot will automatically create tables using JPA.

---

# Database Design

### Users Table

| Column   | Type   |
| -------- | ------ |
| id       | Long   |
| name     | String |
| email    | String |
| password | String |

---

### Bank Accounts Table

| Column         | Type   |
| -------------- | ------ |
| id             | Long   |
| account_number | String |
| balance        | Double |
| user_id        | FK     |


### Transactions Table

| Column     | Type          |
| ---------- | ------------- |
| id         | Long          |
| type       | String        |
| amount     | Double        |
| timestamp  | LocalDateTime |
| account_id | FK            |

Transaction types:

```
DEPOSIT
WITHDRAW
TRANSFER_IN
TRANSFER_OUT
```

---

# API Endpoints

## User APIs

### Register User

```
POST /users/register
```

Request Body:

```json
{
  "name": "Ashar",
  "email": "ashar@gmail.com",
  "password": "123456"
}
```

---

### Get User By ID

```
GET /users/{id}
```

---

### Get All Users

```
GET /users
```

---

# Account APIs

### Create Bank Account

```
POST /accounts/create?userId=1&accountNumber=1234567890
```

---

### Deposit Money

```
POST /accounts/deposit?accountNumber=1234567890&amount=500
```

---

### Withdraw Money

```
POST /accounts/withdraw?accountNumber=1234567890&amount=200
```

Withdraw operation validates **available balance** before processing.

---

### Transfer Money

```
POST /accounts/transfer?fromAccount=1234567890&toAccount=9876543210&amount=100
```

Money transfer uses **transactional processing** to ensure database consistency.

---

# Transaction APIs

### View Transaction History

```
GET /accounts/transactions?accountNumber=1234567890
```

---

### Account Statement (Date Range)

```
GET /accounts/statement?accountNumber=1234567890&startDate=2024-01-01&endDate=2024-12-31
```

Returns all transactions between the given dates.

---

# Global Exception Handling

Centralized exception handling implemented using:

```
@RestControllerAdvice
```

Handles errors such as:

* User not found
* Account not found
* Insufficient balance
* Invalid transaction requests

---

# What I Learned From This Project

* Building REST APIs with Spring Boot
* Connecting Spring Boot with PostgreSQL
* Designing database relationships using JPA
* Implementing business logic in the service layer
* Managing financial transactions safely using `@Transactional`
* Implementing DTO pattern for secure API responses
* Global exception handling
* Designing real-world banking features

---

# Upcoming Features

Planned improvements:

* Password encryption (BCrypt)
* Authentication system
* JWT-based security
* Pagination for transactions
* Swagger API documentation
* Unit & integration testing
* Docker containerization

---

# Project Goal

The goal of this project is to **learn backend architecture deeply and simulate real-world digital banking operations**, focusing on clean design, security, and reliable transaction processing.

---

# Author

**Ashar Arif**

Email: asharxheikh47@gmail.com

LinkedIn: linkedin.com/in/ashararif/