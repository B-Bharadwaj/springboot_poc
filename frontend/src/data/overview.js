// Content for the Overview page, checked against the backend source.
const SWAGGER = { label: 'Swagger UI', href: 'http://localhost:8081/swagger-ui.html' };

export const counts = [
  { label: 'Endpoints', value: 14 },
  { label: 'Entities', value: 4 },
  { label: 'Flyway migrations', value: 4 },
  { label: 'Roles', value: 2 },
  { label: 'Profiles', value: 3 },
];

export const journey = [
  'Tomcat',
  'JWT filter',
  'Security rules',
  'DispatcherServlet',
  'Jackson',
  '@Valid',
  'Controller',
  'Service',
  'Mapper and Repository',
  'Database',
];

export const sections = [
  {
    id: 'foundation',
    title: 'Foundation',
    summary: 'The project setup and the patterns every other part builds on.',
    items: [
      {
        feature: 'Java 21, Spring Boot 4, Maven',
        what: 'Spring Boot 4.1.1 parent; starters for web MVC, data JPA, validation, security, actuator and Flyway; the web starter brings embedded Tomcat.',
        where: [{ label: 'pom.xml', note: 'pom.xml' }],
      },
      {
        feature: 'Layered architecture',
        what: 'controller -> service -> repository, with DTOs and mappers between the web and the database.',
        where: [{ label: 'backend source', note: 'backend source' }],
      },
      {
        feature: 'Constructor injection, beans, IoC',
        what: 'Beans from @Service, @RestController, @Component, @Configuration with @Bean, and Spring Data repository interfaces (no @Repository needed); no field injection.',
        where: [{ label: 'backend source', note: 'backend source' }],
      },
      {
        feature: 'Lombok',
        what: 'Getters, setters, constructors and loggers (@Slf4j) generated at compile time.',
        where: [{ label: 'entities and DTOs', note: 'entities and DTOs' }],
      },
    ],
  },
  {
    id: 'rest-api',
    title: 'REST API',
    summary: '14 endpoints in 4 controllers: auth, products, orders and /hello.',
    items: [
      {
        feature: 'Products CRUD',
        what: 'GET list, GET by id, search by name, cheaper-than filter, POST, PUT, DELETE.',
        where: [{ label: 'Products', to: '/products' }, SWAGGER],
      },
      {
        feature: 'Orders',
        what: 'Place order, paginated list, slow and fast demo endpoints.',
        where: [
          { label: 'Place order', to: '/order' },
          { label: 'Orders', to: '/orders' },
          { label: 'Demo tools', to: '/demo' },
        ],
      },
      {
        feature: 'Auth',
        what: 'Register and login.',
        where: [{ label: 'Login', to: '/login' }],
      },
      {
        feature: 'HTTP status codes',
        what: '200, 201, 204, 400, 401, 403, 404, 409, 500; every endpoint except /hello returns a ResponseEntity.',
        where: [{ label: 'Demo tools (Error demos)', to: '/demo' }],
      },
      {
        feature: 'Request binding',
        what: '@PathVariable, @RequestParam, @RequestBody, plus Pageable and the logged-in Authentication as method parameters.',
        where: [{ label: 'controllers', note: 'controllers' }],
      },
      {
        feature: 'DTOs and mappers',
        what: 'Request and response classes, so entities never leave the service layer; ProductMapper converts both ways, OrderMapper entity to response.',
        where: [{ label: 'dto and mapper packages', note: 'dto and mapper packages' }],
      },
    ],
  },
  {
    id: 'persistence',
    title: 'Persistence (JPA and Hibernate)',
    summary: 'Entities, relationships and queries, including the N+1 problem and its fixes.',
    items: [
      {
        feature: 'Entities',
        what: 'User, Product, Order, OrderItem mapped to the users, products, orders and order_items tables.',
        where: [{ label: 'model package', note: 'model package' }],
      },
      {
        feature: 'Relationships',
        what: '@ManyToOne with @JoinColumn, @OneToMany(mappedBy), cascade, orphanRemoval, LAZY loading; order_items is its own entity because it carries quantity and unit price.',
        where: [{ label: 'Orders', to: '/orders' }],
      },
      {
        feature: 'Derived queries',
        what: 'findByName, findByPriceLessThan, findByEmail, existsByEmail, findByUserEmail.',
        where: [{ label: 'Products', to: '/products' }],
      },
      {
        feature: 'Custom query',
        what: '@Query with a JPQL join fetch (findAllWithItems).',
        where: [{ label: 'Demo tools (Slow vs fast)', to: '/demo' }],
      },
      {
        feature: 'N+1 problem',
        what: 'With the 5 seeded orders, /slow runs 4 queries with batch fetching (12 without) and /fast runs 1 with join fetch; more orders mean more /slow queries.',
        where: [{ label: 'Demo tools', to: '/demo' }],
      },
      {
        feature: '@EntityGraph and batch fetching',
        what: 'Loads the user with the page of orders; default_batch_fetch_size = 10 loads lazy data in groups.',
        where: [{ label: 'backend terminal', note: 'backend terminal' }],
      },
      {
        feature: 'Pagination and sorting',
        what: 'Pageable with page, size and sort, returned in a PageResponse wrapper.',
        where: [{ label: 'Orders', to: '/orders' }],
      },
    ],
  },
  {
    id: 'transactions',
    title: 'Transactions',
    summary: 'Placing an order is all or nothing.',
    items: [
      {
        feature: 'Place order',
        what: '@Transactional: stock check, stock reduction, order and items saved together.',
        where: [{ label: 'Place order', to: '/order' }],
      },
      {
        feature: 'Rollback',
        what: 'Any runtime exception (such as the 409 or 404) undoes all changes, so nothing is half-saved.',
        where: [{ label: 'Place order (try an order that is too large)', to: '/order' }],
      },
      {
        feature: 'Price snapshot',
        what: 'Unit price copied onto each order item at order time.',
        where: [{ label: 'Orders', to: '/orders' }],
      },
      {
        feature: 'Read-only transactions',
        what: '@Transactional(readOnly = true) on the OrderService reads and on login.',
        where: [{ label: 'OrderService and AuthService', note: 'OrderService and AuthService' }],
      },
    ],
  },
  {
    id: 'database',
    title: 'Database and migrations',
    summary: 'PostgreSQL with a versioned schema.',
    items: [
      {
        feature: 'PostgreSQL',
        what: 'Real database; the schema is validated against the entities at startup (ddl-auto: validate).',
        where: [{ label: 'application.yml', note: 'application.yml' }],
      },
      {
        feature: 'Flyway migrations',
        what: '4 numbered SQL files: create tables, add the description column, add auth columns with a unique email index, lower-case emails.',
        where: [{ label: 'db/migration', note: 'db/migration' }],
      },
      {
        feature: 'Data seeder',
        what: 'CommandLineRunner loads 3 products, 3 users and 5 orders once; skipped when products already exist. Seeded users have no password, so they cannot log in.',
        where: [{ label: 'DataSeeder', note: 'DataSeeder' }],
      },
    ],
  },
  {
    id: 'errors',
    title: 'Validation and error handling',
    summary: 'Bad input is rejected early, and every error has the same JSON shape.',
    items: [
      {
        feature: 'Bean Validation',
        what: '@NotNull, @NotBlank, @Size, @Positive, @Min, @Email with clear messages, switched on with @Valid, nested with List<@Valid ...>.',
        where: [{ label: 'Login (register with bad input)', to: '/login' }],
      },
      {
        feature: 'Global exception handler',
        what: 'One @RestControllerAdvice returns every error as the same JSON: timestamp, status, error, message, path, fieldErrors (only when set).',
        where: [{ label: 'Demo tools (Error demos)', to: '/demo' }],
      },
      {
        feature: 'Handled cases',
        what: 'Validation 400, malformed JSON 400, bad URL type 400, invalid sort 400, wrong login 401, access denied 403, not found 404, stock conflict 409, email taken 409, referenced record 409, unexpected 500.',
        where: [{ label: 'Demo tools', to: '/demo' }],
      },
      {
        feature: 'Safe failures',
        what: 'Clients never see stack traces or database text; unexpected errors are logged with the stack trace.',
        where: [{ label: 'backend terminal', note: 'backend terminal' }],
      },
      {
        feature: "Spring's own errors keep their status",
        what: 'Unknown URL 404, wrong method 405, wrong content type 415, missing query parameter 400.',
        where: [{ label: 'GlobalExceptionHandler', note: 'GlobalExceptionHandler' }],
      },
    ],
  },
  {
    id: 'security',
    title: 'Security',
    summary: 'Stateless JWT login with roles.',
    items: [
      {
        feature: 'JWT login',
        what: 'Signed token (HS256) with email, role, issued-at and expiry; stateless, nothing stored on the server.',
        where: [{ label: 'Login', to: '/login' }],
      },
      {
        feature: 'BCrypt',
        what: 'Passwords stored only as salted hashes, checked with matches.',
        where: [{ label: 'users table', note: 'users table' }],
      },
      {
        feature: 'Security filter chain',
        what: 'JwtAuthenticationFilter reads the Bearer token on every request; SecurityConfig sets public and protected paths.',
        where: [{ label: 'Demo tools (call with no token)', to: '/demo' }],
      },
      {
        feature: '401 and 403',
        what: 'An entry point returns 401 in the standard JSON; @PreAuthorize denials become 403 in the exception handler, and an access-denied handler covers the filter chain.',
        where: [{ label: 'Demo tools', to: '/demo' }],
      },
      {
        feature: 'Roles and method security',
        what: 'ADMIN or USER; @EnableMethodSecurity and @PreAuthorize on product create, update, delete and the slow and fast endpoints.',
        where: [{ label: 'Products (admin buttons)', to: '/products' }],
      },
      {
        feature: 'Ownership',
        what: 'An order belongs to the logged-in user taken from the token, and a USER only sees their own orders.',
        where: [{ label: 'Orders', to: '/orders' }],
      },
      {
        feature: 'Hardening',
        what: 'Same message for wrong email or wrong password, a dummy-hash check so both take similar time, password max 72, unique lower-cased email, secrets kept out of Git.',
        where: [{ label: 'AuthService', note: 'AuthService' }],
      },
    ],
  },
  {
    id: 'configuration',
    title: 'Configuration and operations',
    summary: 'Settings per environment, logging and health.',
    items: [
      {
        feature: 'Profiles',
        what: 'dev, prod and a git-ignored local profile; secrets come from environment variables or the local file.',
        where: [{ label: 'application-*.yml', note: 'application-*.yml' }],
      },
      {
        feature: 'Typed configuration',
        what: '@ConfigurationProperties record for app.greeting and app.jwt (secret, expiration), found by @ConfigurationPropertiesScan.',
        where: [{ label: 'AppProperties', note: 'AppProperties' }],
      },
      {
        feature: 'Logging',
        what: 'SLF4J levels per profile; in dev, SQL is printed and a [REQ] and [RES] line wraps every request.',
        where: [{ label: 'backend terminal', note: 'backend terminal' }],
      },
      {
        feature: 'open-in-view disabled',
        what: 'Lazy loading only inside transactions.',
        where: [{ label: 'application.yml', note: 'application.yml' }],
      },
      {
        feature: 'Actuator',
        what: 'Public health endpoint.',
        where: [{ label: '/actuator/health', href: 'http://localhost:8081/actuator/health' }],
      },
    ],
  },
  {
    id: 'api-docs',
    title: 'API documentation',
    summary: 'Generated, browsable API docs.',
    items: [
      {
        feature: 'OpenAPI and Swagger UI',
        what: 'Generated docs for every endpoint (springdoc).',
        where: [SWAGGER],
      },
      {
        feature: 'Documentation annotations',
        what: '@Tag, @Operation, @ApiResponse, @Schema.',
        where: [{ label: 'controllers and DTOs', note: 'controllers and DTOs' }],
      },
      {
        feature: 'Bearer auth in Swagger',
        what: 'An Authorize button sends the token (@SecurityScheme and @SecurityRequirement).',
        where: [SWAGGER],
      },
      {
        feature: 'Docs off in prod',
        what: 'Swagger UI and the /v3/api-docs JSON are disabled in the prod profile.',
        where: [{ label: 'application-prod.yml', note: 'application-prod.yml' }],
      },
    ],
  },
  {
    id: 'internals',
    title: 'Spring Boot internals understood',
    summary: 'What Spring Boot does on its own, and how to see it.',
    items: [
      {
        feature: 'Auto-configuration',
        what: 'The condition report shows why Tomcat, the DataSource, Flyway, Hibernate and Jackson start on their own; UserDetailsServiceAutoConfiguration is excluded on purpose (no default user or generated password).',
        where: [{ label: '--debug', note: '--debug' }],
      },
      {
        feature: 'Request journey',
        what: 'See the strip at the top of the page.',
        where: [{ label: 'this page', note: 'this page' }],
      },
    ],
  },
  {
    id: 'not-implemented',
    title: 'Not implemented yet',
    summary: 'Future work, listed honestly.',
    items: [
      {
        feature: 'Automated tests, refresh tokens, caching, async processing, Docker',
        what: 'Planned next steps; only the default contextLoads smoke test exists today.',
        where: [{ label: 'roadmap', note: 'roadmap' }],
      },
    ],
  },
];
