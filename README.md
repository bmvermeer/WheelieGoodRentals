# Wheelie Good Rentals

Wheelie Good Rentals is a vehicle rental service application built using Spring Boot. This application allows users to book vehicles, manage their profiles, and upload documents. It also includes an admin interface for managing users.

## Features

- User registration and authentication
- Profile management
- Vehicle booking
- Document upload and management
- Admin interface for user management
- Chat interface for customer support using an LLM with Langchain4J

## Technologies Used

- Java
- Spring Boot
- Langchain4J
- H2 Database
- Maven
- Thymeleaf
- Bootstrap

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/bmvermeer/wheelie-good-rentals.git
    cd wheelie-good-rentals
    ```

2. Create an `application.properties` file in the `src/main/resources` directory with the following content:
    ```properties
    spring.application.name=WheelieGoodRentals

    server.port=8080
    server.servlet.session.cookie.http-only=false

    spring.h2.console.enabled=true
    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.jpa.show-sql=false

    spring.security.user.name=brian
    spring.security.user.password=

    spring.jpa.defer-datasource-initialization=true

    spring.thymeleaf.cache=false

    openai.api.key=
    ```

3. Build the project:
    ```sh
    mvn verify
    ```

4. Run the application:
    ```sh
    mvn spring-boot:run
    ```

### Accessing the Application

- The application will be available at `http://localhost:8080`.

## License

This project is licensed. See the [LICENSE](LICENSE.md) file for details.

## Contact

For any inquiries or issues, please contact [Brian Vermeer](https://brianvermeer.nl).