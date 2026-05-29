## Secure Digital Banking Platform
Secure digital banking platform solve real-world risks like unauthorized logins, and unsafe money transfers by enforcing strict
verification before any transaction is completed.
---

### Tech Stack

#### Backend:

* Java 17
* Spring Boot 3
* Spring Security
* Spring Data JPA
* Hibernate
* Maven

#### Database:

* PostgreSQL

#### Cache & Messaging:

* Redis
* Apache Kafka
* Zookeeper

#### DevOps:

* Docker
* Docker Compose
* Jenkins

#### Monitoring:

* Prometheus
* Grafana

---

<details>
<summary>Features</summary>

<br>

#### Banking Features

* User Registration & Login
* JWT Authentication
* Bank Account Creation
* Deposit & Withdraw
* Money Transfer
* OTP-based Transfer Verification
* Transaction History / Statement
* Account Freeze Support
* Account Lookup
* Pagination Support

---

#### Security Features

* Spring Security + JWT
* Role-Based Authorization (USER / ADMIN)
* Login Rate Limiting using Redis
* OTP Expiration using Redis
* Protected APIs
* Password Encryption (BCrypt)

---

#### Admin Features

* View All Users
* Freeze/Unfreeze Accounts
* Fraud Monitoring
* Audit Logs
* Transaction Tracking

---

#### Fraud Detection:

* Large Transfer Monitoring
* Fraud Alert Email Notification
* Suspicious Activity Tracking

---

#### Event-Driven Architecture:

Transfer events are published to Kafka and processed asynchronously by multiple consumers.

```text
Transfer Event
    ↓
Kafka Topic
    ↓
Fraud Consumer
    ↓
Email Consumer
    ↓
Audit Consumer
```

---

#### Redis Features:

* OTP Cache
* Pending Transfer Cache
* Login Rate Limiting
* Fast Session Storage
* Temporary Data Storage

---

#### Observability:

* Spring Boot Actuator
* Prometheus Metrics
* Grafana Dashboards
* API Monitoring
* JVM Monitoring
* Request Metrics

---

</details>


---

<details>
<summary>Database Design</summary>

<br>

#### User Table:

| Column     | Type      |
| ---------- | --------- |
| id         | BIGINT    |
| name       | VARCHAR   |
| email      | VARCHAR   |
| password   | VARCHAR   |
| role       | ENUM      |
| created_at | TIMESTAMP |

---

#### Bank Account Table:

| Column         | Type    |
| -------------- | ------- |
| id             | BIGINT  |
| account_number | VARCHAR |
| balance        | DOUBLE  |
| account_type   | ENUM    |
| frozen         | BOOLEAN |
| user_id        | BIGINT  |

---

#### Transaction Table:

| Column           | Type      |
| ---------------- | --------- |
| id               | BIGINT    |
| from_account     | VARCHAR   |
| to_account       | VARCHAR   |
| amount           | DOUBLE    |
| transaction_type | VARCHAR   |
| created_at       | TIMESTAMP |

---

#### Fraud Alert Table:

| Column     | Type      |
| ---------- | --------- |
| id         | BIGINT    |
| message    | VARCHAR   |
| severity   | VARCHAR   |
| created_at | TIMESTAMP |

---

#### Audit Log Table:

| Column      | Type      |
| ----------- | --------- |
| id          | BIGINT    |
| action      | VARCHAR   |
| description | VARCHAR   |
| created_at  | TIMESTAMP |

</details>

---

<details>
<summary>API Endpoints</summary>

<br>

#### Authentication APIs:

Register User
`POST /users/register`

Login User
`POST /users/login`

---

#### Banking APIs:

Create Account
`POST /accounts/create`

Deposit Money
`POST /accounts/deposit`

Withdraw Money
`POST /accounts/withdraw`

Transfer Money
`POST /accounts/transfer`

Verify OTP Transfer
`POST /accounts/transfer/verify`

Account Statement
`GET /accounts/statement`

Get My Accounts
`GET /accounts/my`

---

#### Admin APIs:

Get All Users
`GET /admin/users`

Freeze Account
`POST /admin/freeze/{accountNumber}`

Unfreeze Account
`POST /admin/unfreeze/{accountNumber}`

Fraud Alerts
`GET /admin/fraud-alerts`

</details>

---

### Setup Guide

#### 1. Clone Repository

```bash
git clone <https://github.com/asharxh/Secure-Digital-Banking-Platform.git>
cd Secure-Digital-Banking-Platform
```

---

#### 3. Run with Docker

```bash
docker compose up --build
```

---

#### 4. Access Services

| Service     | URL                                         |
| ----------- | ------------------------------------------- |
| Application | http://localhost:8080                       |
| Swagger UI  | http://localhost:8080/swagger-ui/index.html |
| Kafka UI    | http://localhost:8090                       |
| Prometheus  | http://localhost:9090                       |
| Grafana     | http://localhost:3000                       |

---

<details>
<summary>Screenshots</summary>

<br>

#### Swagger UI

![Swagger UI](./screenshots/swagger-ui.png)

---

#### Kafka UI

![Kafka UI](./screenshots/kafka-ui.png)

---

#### Grafana Dashboard

![Grafana Dashboard](./screenshots/grafana-dashboard.png)

---

#### Prometheus Metrics

![Prometheus Metrics](./screenshots/prometheus.png)

---

#### System Architecture

![Architecture](./screenshots/architecture.png)

</details>

---

### Author

#### Ashar Arif
GitHub:
https://github.com/asharxh

LinkedIn:
https://linkedin.com/in/ashararif

---