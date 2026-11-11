# Order POC frontend

A small React (Vite) app for demoing the Spring Boot API.

## Run

1. Start the backend from the project root (it listens on port 8081):

   ```
   ./mvnw spring-boot:run
   ```

2. In another terminal, start the frontend:

   ```
   cd frontend
   npm install
   npm run dev
   ```

3. Open http://localhost:5173.

The Vite dev server forwards every `/api/...` request to `http://localhost:8081`, so the browser only talks to port 5173 and the backend needs no CORS setup.

`npm run build` writes a production build to `dist/`.

## How to read the demo

Each page shows a grey "Endpoints used" strip under its title, and each button that calls the backend shows its endpoint in small grey text. The "API activity" panel pinned to the bottom lists every call the browser made, newest first, with its status (green 2xx, orange 4xx, red 5xx or no connection) and time in ms. In the backend terminal (dev profile), each call prints one `[REQ]` line with the user and role, then the SQL it ran, then one `[RES]` line with the status and time. So one click gives one row in the panel and one `[REQ]`…`[RES]` block in the terminal. Some actions make two calls on purpose: placing an order, saving or deleting a product reloads the product list afterwards.

## Page → user action → endpoint → expected status

| Page | User action | Endpoint | Expected status |
|---|---|---|---|
| Login | Log in with a correct password | `POST /api/auth/login` | 200, then `GET /api/products` 200 |
| Login | Log in with a wrong password | `POST /api/auth/login` | 401 |
| Login | Register a new user | `POST /api/auth/register` | 201 |
| Login | Register with invalid fields | `POST /api/auth/register` | 400 (errors under inputs) |
| Login | Register an email already used | `POST /api/auth/register` | 409 |
| Products | Open the page | `GET /api/products` | 200 |
| Products | Search by name | `GET /api/products/search?name=X` | 200 |
| Products | Cheaper than | `GET /api/products/cheap?maxPrice=N` | 200 |
| Products | ADMIN: Save a new product | `POST /api/products` | 201 (400 if invalid) |
| Products | ADMIN: Save an edited product | `PUT /api/products/{id}` | 200 (400 if invalid) |
| Products | ADMIN: Delete an unused product | `DELETE /api/products/{id}` | 204 |
| Products | ADMIN: Delete a product used in orders | `DELETE /api/products/{id}` | 409 |
| Place order | Open the page | `GET /api/products` | 200 |
| Place order | Place a valid order | `POST /api/orders` | 201, then `GET /api/products` 200 |
| Place order | A line with quantity 0 | `POST /api/orders` | 400 (error under that line) |
| Place order | More than the stock | `POST /api/orders` | 409 |
| Orders | Open, change page, size or sort | `GET /api/orders/page?page=0&size=5&sort=createdAt,desc` | 200 |
| Demo tools | Run slow / Run fast | `GET /api/orders/slow`, `GET /api/orders/fast` | 200 as ADMIN, 403 as USER |
| Demo tools | Delete product 999999 | `DELETE /api/products/999999` | 403 as USER, 404 as ADMIN |
| Demo tools | Page with an invalid sort | `GET /api/orders/page?sort=nonsense` | 400 |
| Demo tools | Call products with no token | `GET /api/products` (no header) | 401, you stay logged in |

## Demo script

You need one USER account (register in the app) and one ADMIN account (an existing user whose role is ADMIN in the database).

1. **Register with bad data:** on the Register tab, submit an empty name, the email `abc` and the password `123`. An error appears under each field. Then register properly. The app switches to Login and shows a green message.
2. **Wrong password:** log in with a wrong password. "Invalid email or password" appears under the form (401).
3. **Log in as the USER:** the navbar shows the email and a USER badge. The Products page has no Add, Edit or Delete buttons.
4. **Search and filter:** search a product name (exact match, case sensitive), then "Cheaper than" a price. Use Clear to show everything again.
5. **Place an order:** on Place order, add two lines and submit. A confirmation card shows the order id and items, and the stock in the dropdown goes down.
6. **Validation on lines:** set line 2's quantity to 0 and submit. The error appears under line 2.
7. **Not enough stock (409):** order more than the stock shown. A red banner shows the backend's message.
8. **Orders:** open Orders. Change the page size and sort, page through with Previous and Next, and click a row to see its items. A USER sees only their own orders.
9. **Demo tools as USER:** "Slow vs fast" shows the 403 banner. "Delete product 999999" gives 403. "Page with an invalid sort" gives 400. "Call products with no token" gives 401, and you stay logged in.
10. **Log out, then log in as the ADMIN:** Add, Edit and Delete appear. Add a product with an empty name and price 0 to see the field errors, then save a valid one. Edit it, then delete it (a confirm dialog appears first).
11. **Demo tools as ADMIN:** run slow and fast and compare the times (slow = lazy loading, fast = join fetch). "Delete product 999999" now gives 404.
12. **Session expiry:** in DevTools (Application → Session Storage), change the `token` value to any other text, or wait 60 minutes for it to expire. Then click any link. You land on /login with "Session expired, please log in again".
