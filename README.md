# Fitness Tracker API

A Spring Boot-based RESTful API for managing users, workout plans, and activity logs in a fitness tracking system.

## Features

- User registration and management
- Workout plan creation and tracking
- Logging of fitness activities
- Input validation and exception handling
- API documentation with Swagger

## Technologies Used

- Java 17+
- Spring Boot 3.4.3
- Spring Data JPA
- H2 (in-memory)
- Maven
- Swagger for API documentation

## Setup Instructions

### 1. Clone the repository

- Use **development/current** branch

```bash
git clone https://github.com/VivekVarmora/fitness-tracker.git
cd fitness-tracker
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Run the application

```bash
mvn spring-boot:run
```

## Access the Application

- **API Base URL:** `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **Test Covrage Report Path:** `tracker/TestCovrage-Report/site/jacoco-merged/index.html`

## API Usage Guide

---

## Authentication & Authorization Rules

| Endpoint                        | Access Level                            |
|----------------------------------|------------------------------------------|
| `POST /fitness/users`           | Public – No authentication required    |
| `GET /fitness/users`            | Authenticated – Requires `ADMIN` role |
| All other endpoints_		        |  Authenticated – Requires `ADMIN` or `USER` role |

---

### User Endpoints

- `POST fitness/users` – Create a new user  
- `GET fitness/users/{id}` – Retrieve user by ID  
- `PUT fitness/users/{id}` – Update user info  
- `DELETE fitness/users/{id}` – Delete user  
- `GET fitness/users` – List all users  

### Workout Plan Endpoints

- `POST fitness/workout-plans` – Create a new workout plan  
- `GET fitness/workout-plans/{id}` – Get workout by userId  
- `PUT fitness/workout-plans/{id}` – Update workout plan  
- `DELETE fitness/workout-plans/{id}` – Delete workout  
- `GET fitness/workout-plans` – List all workout plans  

### Activity Log Endpoints

- `POST fitness/activity` – Log a new activity  
- `GET fitness/activity/user/{id}` – Get activity by userId  
- `PUT fitness/activity/{id}` – Update activity  
- `DELETE fitness/activity/{id}` – Delete activity  
- `GET fitness/activity` – List all activity logs 
