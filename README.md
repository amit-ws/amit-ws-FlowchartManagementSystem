# Flowchart Management System

## Setup Instructions

### 1. Pull the code and load it in IntelliJ or your favorite editor with Spring related setups.

### 2. Tools Information:

- **JDK**: 17
- **Spring Boot**: 3.4+
- **Spring Framework**: 6.0.x
- **Spring Data JPA**
- **H2 Database**
- **Springdoc OpenAPI**: 2.7.0
- **Lombok**: 1.18.34
- **Spring Boot Starter Validation**: 3.4.0

### 3. Run the program:

Find `FlowchartManagementSystemApplication` main class to start the application.

### 4. Access:

- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)
- **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
    - **Credentials**:
        - Username: `sa`
        - Password: `pass123`
        - JDBC URL: `jdbc:h2:mem:flowchartDb`

### 5. ER Diagram

### 6. Major Functionalities covered

### 7. APIs with functionalities Overview

### 8. APIs Flow

#### 8.1 Register User

**Endpoint**

```http
POST /api/users/v1/register
```

**Payload**

```json
{
  "username": "John Doe",
  "email": "demo@gmail.com",
  "password": "Pass@123"
}
```

<br>
---

> **API Flow Suggestion:**
> * Apart from below api flow, use the available GET endpoints to fetch details

**Start with user registration**

```http
POST /api/users/v1/register
```

**Create flowcharts**

```http
POST /api/flowcharts/v1/create
```

***Create Node(s)**

```http
POST /api/nodes/v1/create
```

**Create connections between node**

```http
POST /api/nodeConnections/v1/create
```

**Get Flowchart details with Nodes, and connections details**

```http
GET /api/flowcharts/v1/getById
```
