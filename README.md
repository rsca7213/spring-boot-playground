# Spring Boot Playground

This is a simple Spring Boot REST API developed for learning purposes.

## 🚀 Getting Started:

1. Clone the git repository:
   ```bash
   git clone https://github.com/rsca7213/spring-boot-playground
   
2. Navigate to the project directory:
   ```bash
   cd spring-boot-playground
   ```
   
3. Open the project in your favorite IDE (e.g., IntelliJ IDEA, Eclipse).

4. Ensure you have the required dependencies installed:
   - Java JDK 21 (Amazon Corretto 21 preferred)
   - Gradle 8.14.1 or higher
   - PostgresSQL 17.5 or higher

5. Create a `.env` file in the root directory based on the provided `.env.example`

6. Run the application using Gradle:
   ```bash
   ./gradlew bootRun
   ```
   
7. The application will start and be accessible.
   - For development, the application will run on `http://localhost:8080`

8. Optionally, you can perform a Health Check on the application:
   - Open a browser and navigate to: `/api/actuator/health`
   - **For Development:** `http://localhost:8080/api/actuator/health`

## 🗃️ Database Information:

Check out Flyway's documentation for more details: [Flyway Documentation](https://flywaydb.org/documentation/)

- **Migrations:** Flyway is used for database migrations.
  - Located at: `src/main/resources/database/migrations`
  - Migrations are automatically applied on application startup.
- **Seeding:** The application is automatically seeded on **Development** with consistant testing data.
  - Located at: `src/main/resources/database/seeds/dev`
  - When the application starts on development, it will clean and re-seed the database with the data.

## 📗 Accesing the Swagger UI (Documentation):

1. Run the application with Gradle
2. Open a browser and navigate to: `/api/docs/ui`
   - **For Development:** `http://localhost:8080/api/docs/ui`

## 🛫 Features:

- CRUD operations on a simple Product entity
- An example implementation of an Insurance Policy claim creation flow
- User and Role management
- Authentication and Authorization using JWT and Spring Security
- Database migrations using Flyway
- Health checks using Spring Boot Actuator
- API documentation using OpenAPI 3.0 (Swagger UI)
- Unit testing with JUnit 5 and Mockito
- Cloud storage integration with AWS S3 for file uploads
- Front-end integration with Angular Playground
- Environment configuration using `.env` files

## 🛜 Additional repositories:

- **Front-End**: [Angular Playground](https://github.com/rsca7213/angular-playground)
  - Developed with Angular 20, TypeScript 5 and Tailwind CSS 4

## 🧪 Running Tests:

1. Ensure the application is not running.
2. Run the tests using Gradle:
   ```bash
   ./gradlew test
   ```
3. The test results will be displayed in the console.
4. You can also run specific tests or test classes by specifying them in the command:
   ```bash
   ./gradlew test --tests "com.playground.api.ProductServiceTests"
   ```

## 📦 Project dependencies:

- **Runtime:** Java JDK 21 (Amazon Corretto 21 preferred)
- **Database:** PostgresSQL 17.5 or higher
- **Main Framework:** Spring Boot 3.4.5 or higher
- **Additional Libraries:**
  - Spring Data JPA
  - Spring Security
  - Spring Boot Actuator
  - Spring Boot DevTools
  - Flyway 
  - Lombok
  - Spring Documentation
  - AWS SDK v2 (for S3)
  - JUnit 5 (for testing)
  - Mockito (for testing)
  - Json Web Token (JWT)
- **Dependencies and Build:** Gradle 8.14.1 or higher (with Groovy)