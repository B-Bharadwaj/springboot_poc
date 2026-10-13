# Order Management POC (learning project)

Stack: Java 21, Spring Boot, Maven, PostgreSQL (H2 for early days).

## Rules
- This is a learning project. Before writing code for a new concept, explain it in 3-4 lines.
- Keep changes small: one concept per change, then stop and wait.
- Layers: controller -> service -> repository. No business logic in controllers.
- Constructor injection only. Never expose JPA entities in API responses; use DTOs.
- Add a short comment above each annotation the first time it is used, saying what it does.
- Write a test with every feature once we reach Phase 5.
- After each task, list: files changed, what to run, and what I should be able to explain.

## Commands
- Run: ./mvnw spring-boot:run
- Test: ./mvnw test