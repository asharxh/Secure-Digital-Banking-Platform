
---

# Secure Digital Banking Platform

A backend banking system built using Spring Boot and PostgreSQL.

This project is being built step-by-step to understand how real-world banking systems work — including user management, account creation, and money transactions.

---

## Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Maven
* Lombok

---

## Current Features (Phase 1)

User Registration,
Bank Account Creation,
Deposit Money,
Database Integration (PostgreSQL)

---

## Project Structure

```
com.example.bank
 ├── entity
 ├── repository
 ├── service
 ├── controller
```

The project follows a clean layered architecture:

Controller → Service → Repository → Database

---

## Database Configuration

Make sure PostgreSQL is running and create database:

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
```

---

## API Endpoints

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

## What I Learned So Far

* How Spring Boot connects to PostgreSQL
* How JPA creates tables automatically
* How to design entity relationships
* How to structure a backend project cleanly
* How to build service-layer business logic

---

## Upcoming Features

* Withdraw Money
* Transfer Money Between Accounts
* Transaction History
* Exception Handling
* Input Validation
* Authentication & Authorization (Spring Security + JWT)
* Unit Testing

---

## Project Goal

The goal of this project is to understand backend architecture deeply and simulate real banking operations in a secure and scalable way.

---

## 👨‍💻 Author

Ashar Arif (
Java Developer )

Email: asharxheikh47@gmail.com

LinkedIn: linkedin.com/in/ashararif/