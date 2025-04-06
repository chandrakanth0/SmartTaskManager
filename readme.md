
---

# 🗂️ Task Manager API

A Spring Boot-based Task Manager application with role-based access control (Admin and User roles), integrated logging, and RESTful endpoints for managing and assigning tasks. The backend is built using Java and tested with Postman. No frontend is currently implemented.

---

## 🔧 Technologies Used

- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- H2 / MySQL (configurable)
- SLF4J / Logback (for logging)
- Mockito + JUnit (for testing)
- Postman (for API testing)

---

## 🔐 Roles

- **Admin**
  - Create, update, delete tasks.
  - Assign tasks to users.
  - View all tasks.
- **User**
  - View their own tasks.
  - Mark tasks as completed.
  - Update title and description of their assigned tasks.

---

## 📂 Project Structure

```
src/
├── controller/          # REST Controllers
├── entity/              # JPA Entities (Task, User)
├── repository/          # Spring Data JPA Repositories
├── service/             # Business logic (if added)
├── config/              # Security & Logging Configuration
```

---

## 📌 Features

- User Registration with encrypted passwords
- JWT-based authentication (or session if implemented)
- Role-based endpoint access
- Real-time log file with timestamped filename: `app-yyyy-MM-dd_HH-mm-ss.log`
- Centralized Exception Handling
- Easily extendable and testable architecture

---

## 🔗 API Endpoints

### 📥 Auth

| Method | Endpoint           | Description              |
|--------|--------------------|--------------------------|
| POST   | `/auth/register`   | Register a new user      |

---

### 👤 User APIs

| Method | Endpoint                        | Description                                |
|--------|----------------------------------|--------------------------------------------|
| GET    | `/user/tasks/my/{userId}`       | Get all tasks assigned to a user           |
| PUT    | `/user/tasks/complete/{taskId}` | Mark a task as completed                   |
| GET    | `/user/tasks/{taskId}`          | View a specific task                       |
| PUT    | `/user/tasks/update/{taskId}`   | Update task's title & description          |

---

### 🛠️ Admin APIs

| Method | Endpoint                                      | Description                    |
|--------|-----------------------------------------------|--------------------------------|
| POST   | `/admin/tasks/create`                         | Create a task                  |
| PUT    | `/admin/tasks/update/{id}`                    | Update task info               |
| DELETE | `/admin/tasks/delete/{id}`                    | Delete a task                  |
| PUT    | `/admin/tasks/assign/{taskId}/user/{userId}`  | Assign task to user            |
| GET    | `/admin/tasks/all`                            | Get all tasks                  |

---

## ✅ Testing with Postman

- Import your API collection and run tests for all endpoints.
- Example test cases:
  - User registration
  - Admin creates & assigns task
  - User marks task as completed
  - Unauthorized access rejection

---

## 🪵 Logging

Logs are stored with a unique timestamp per run in the format:

```
logs/app-2025-04-06_14-22-30.log
```

All critical operations (registering users, assigning tasks, errors) are logged using SLF4J and Logback.

---

## ⚠️ Error Handling

Centralized error handling using `@ControllerAdvice`:
- Returns consistent error messages
- Handles common exceptions like:
  - `EntityNotFoundException`
  - `AccessDeniedException`
  - `IllegalArgumentException`

---

## 🚀 Getting Started

1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/task-manager-api.git
   ```

2. Navigate into the project:
   ```bash
   cd task-manager-api
   ```

3. Run the app:
   ```bash
   ./mvnw spring-boot:run
   run the taskmanager main file as spring app
   ```

4. Use Postman to test endpoints.

---

## 👥 Author

- Built by Chandrakanth S
- For learning and productivity enhancement as an Final project at Incture technology limited

---
