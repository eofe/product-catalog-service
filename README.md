# 🛒 Product Catalog Service

> ⚠️ This is a side project I'm building to explore Contract-First and Test-Driven Development using Spring Boot.

A backend microservice for managing product categories, built using Spring Boot. This service is part of the **Ostia** side project.

## 🚀 Features

- ✅ RESTful API for managing product categories
- ✅ Full CRUD support
- ✅ OpenAPI-driven (Contract-First) development
- ✅ Test-driven development (TDD)
- ✅ Profile-based configuration (`dev`, `test`)
- ✅ H2 in-memory database for testing
- ✅ SQL schema and seed data loading

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/ostia/catalog/
│   │   ├── category/            # Category domain (entity, DTO, controller, service)
│   │   └── shared/              # Shared utilities like BaseEntity, ApiVersion
│   └── resources/
│       ├── application.properties
│       ├── schema.sql
│       └── data.sql
├── test/
│   ├── java/...                 # Unit and integration tests
│   └── resources/
│       └── application-test.properties
```

## ⚙️ Technologies

- Java 21
- Spring Boot
- Spring Data JPA
- H2 Database
- OpenAPI (Contract-First)
- JUnit & AssertJ for testing

## 🧪 Running Tests

```bash
mvn test
```

## 🏁 Getting Started

### Run Locally

```bash
./mvnw spring-boot:run
```

The API will be available at:  
`http://localhost:8080/api/v1/categories`

### Sample Request (Create Category)

```json
POST /api/v1/categories
Content-Type: application/json

{
  "name": "Electronics",
  "description": "Devices and accessories"
}
```

## 📄 API Documentation

The OpenAPI contract lives under `/src/main/resources/static/openapi.yaml` (or similar path depending on how you manage it). You can generate Swagger UI or use tools like Postman to explore.

## 🧰 Future Plans

- Add product management
- Secure endpoints with OAuth2 / Keycloak
- Containerize with Docker
- Add support for pagination and filtering

## 🧑‍💻 Author

**Hamza W. Amentag**  
[LinkedIn](https://www.linkedin.com/in/hamza-3s/)  
Built as part of the **Ostia** side project

## 🪪 License

This project is open-source under the MIT License.