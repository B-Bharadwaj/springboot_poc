# Order Management POC

A small order management REST API built while learning Spring Boot. It uses Spring Boot 4 on Java 21, stores data in PostgreSQL with Flyway migrations, and secures endpoints with JWT logins. A React demo app in [`frontend/`](frontend/) calls the API and shows each request as it happens.

## Features

- Layered API: controller → service → repository, with DTOs in requests and responses
- JPA entities with relationships (users, products, orders and order items)
- Transactions around operations that change several rows, such as placing an order
- Request validation and a global error handler that returns consistent JSON errors
- JWT login with `USER` and `ADMIN` roles
- Swagger UI with interactive API docs

## Prerequisites

- JDK 21
- PostgreSQL, with an empty database named `orderdb`
- Node.js 18 or newer (for the frontend only)

Maven is not needed: the project includes the Maven wrapper (`mvnw`).

## Configuration

The database login and the JWT signing key are kept out of the repository in `src/main/resources/application-local.yml`, which is listed in `.gitignore`.

1. Copy the example file:

   ```bash
   cp src/main/resources/application-local.yml.example src/main/resources/application-local.yml
   ```

2. Fill in your own values:

   ```yaml
   spring:
     datasource:
       username: your-db-username
       password: your-db-password

   app:
     jwt:
       # At least 32 characters; use a long random string.
       secret: replace-with-a-long-random-secret
   ```

**Never commit `application-local.yml`.** You can also set the `DB_USERNAME`, `DB_PASSWORD` and `JWT_SECRET` environment variables instead of using the file.

The database URL (`jdbc:postgresql://localhost:5432/orderdb`) is in `application.yml`. Flyway creates the tables on the first start.

## Running

### Backend

```bash
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run`. The API runs on http://localhost:8081.

To run the tests:

```bash
./mvnw test
```

### Frontend

With the backend running, in a second terminal:

```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173. The Vite dev server forwards `/api` requests to the backend on port 8081. See [`frontend/README.md`](frontend/README.md) for more.

## Creating an ADMIN user

New accounts always get the `USER` role. To make an admin:

1. Register an account, either with the frontend or with the API:

   ```bash
   curl -X POST http://localhost:8081/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{"name": "Admin", "email": "admin@example.com", "password": "choose-a-password"}'
   ```

2. Change its role in the database:

   ```sql
   UPDATE users SET role = 'ADMIN' WHERE email = 'admin@example.com';
   ```

3. Log in again (`POST /api/auth/login`). The role is stored in the JWT, so a token issued before the change still has the `USER` role.

## API documentation

With the backend running, open Swagger UI at http://localhost:8081/swagger-ui.html.

To call protected endpoints, log in, copy the token, click **Authorize** and paste the token. Swagger UI is turned off in the `prod` profile.
