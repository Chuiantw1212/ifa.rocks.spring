# AGENTS.md - COLA Light 5.0 Project Instructions

## Project Context
Architecture: COLA Light 5.0 (Clean Object-Oriented & Layered Architecture, lightweight package-based isolation pattern).
Frameworks: Spring Boot 3.x, JDK 17/21.
Paradigm: Domain-Driven Design (DDD) integrated with Command Query Responsibility Segregation (CQRS).
Module Structure: Single Maven module. Architectural boundaries are STRICTLY enforced via package namespaces (adapter, app, domain, infrastructure, client).

## Package Structure & Layer Responsibilities
When generating code, analyzing logic, or performing refactoring, you MUST strictly adhere to the following package boundaries and dependency rules:

### 1. adapter (Adapter Layer)
Sub-packages: adapter.web, adapter.wireless.
Contents: Spring MVC @RestController and routing mechanisms.
Rules:
- ABSOLUTELY NO business logic or database queries here.
- Controllers must route incoming HTTP requests to the app layer using Executor or Service interfaces.
- Return Type Constraint: All endpoints MUST wrap their responses using COLA's SingleResponse, MultiResponse, or PageResponse from the cola-component-dto dependency.

### 2. app (Application Layer)
Sub-packages: app.executor, app.consumer, app.scheduler.
Contents: Use-case orchestration and CQRS handlers.
Rules:
- Implement CQRS: Create separate classes for Commands (modifying state, e.g., AddCustomerCmdExe) and Queries (read-only, e.g., CustomerListQryExe).
- Queries are allowed to bypass the domain layer and interact directly with the infrastructure layer for optimized reads.
- Commands MUST orchestrate Domain Entities and Domain Services.
- Apply @CatchAndLog (from cola-component-catchlog-starter) to handler classes/methods for unified logging and exception management.

### 3. domain (Domain Layer - THE CORE)
Sub-packages: domain.model, domain.ability, domain.gateway.
Contents: Aggregate Roots, Entities, Value Objects, Domain Services, and Gateway Interfaces (Anti-Corruption Layer).
Rules:
- PURITY CONSTRAINT: This package is PURE JAVA. DO NOT use Spring annotations (like @Autowired, @Component) or database annotations (like @Table, @Column).
- Models must be rich (encapsulate behavior and state validations); avoid anemic domain models.
- domain.gateway contains only interface definitions. Implementations belong in the infrastructure layer.

### 4. infrastructure (Infrastructure Layer)
Sub-packages: infrastructure.gatewayimpl, infrastructure.mapper, infrastructure.config.
Contents: Implementations of domain.gateway interfaces, database mappers (MyBatis/JPA), and external RPC clients.
Rules:
- Responsible for converting DO (Data Objects) to Domain Entities and vice versa.
- This layer handles all technical details (cache, DB, MQ) and shields the domain layer from these specifics.

### 5. client (Client SDK Layer)
Contents: DTOs (Data Transfer Objects), Command objects, Query objects, and API interfaces exposed to external consumers.

## Coding Standards & Advanced Components
1. Dependency Injection: Use Constructor Injection via final fields (or Lombok's @RequiredArgsConstructor). DO NOT use @Autowired on fields.
2. Dynamic Routing / Extension Points (cola-component-extension-starter):
- When encountering variable business rules (e.g., different logic for different tenant IDs or user levels), DO NOT use if-else or switch statements.
- Define an interface extending ExtensionPointI.
- Annotate specific implementations with @Extension(bizId="...", useCase="...", scenario="...").
- Invoke the logic using COLA's Extension Executor to ensure Open-Closed Principle compliance.
3. Exception Handling: Throw BizException for business rule violations and SysException for technical infrastructure failures. Do not catch these explicitly in controllers; rely on the CatchLog AOP component to format the error response.

## Development Workflow & Commands
Whenever you generate or modify code, ensure you use the following commands to verify architectural integrity:
- Build Project: mvn clean install -DskipTests
- Run Unit Tests: mvn test
- Run Architecture Tests: mvn test -Dtest=*ArchitectureTest (CRITICAL: Run this to verify no layer boundary violations, such as domain depending on infrastructure, occurred during your modifications).
- Run the Application: mvn spring-boot:run

## Task Execution Strategy
When asked to implement a new feature:
1. Define the Request/Response DTOs and the Command/Query in the client package.
2. Define the Domain Model and Gateway interface in the domain package.
3. Implement the Gateway in the infrastructure package.
4. Create the CQRS Executor in the app package.
5. Expose the endpoint in the adapter package.
6. Run ArchUnit tests to validate compliance.