# Movie Management API

A Spring Boot REST API for managing movies built with Java 17, Maven, Spring Web, Spring Data JPA, and H2 database.

## Features

- CRUD operations for movies
- Search movies by genre, director, or release year
- Comprehensive exception handling
- Input validation
- Unit tests with JUnit 5 and Mockito
- Clean architecture principles

## Technology Stack

- Java 17
- Spring Boot 3.1.5
- Spring Web
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- JUnit 5
- Mockito

## Project Structure

```
src/
├── main/
│   ├── java/com/movie/management/
│   │   ├── MovieManagementApplication.java
│   │   ├── controller/
│   │   │   └── MovieController.java
│   │   ├── entity/
│   │   │   └── Movie.java
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── MovieNotFoundException.java
│   │   │   └── MovieAlreadyExistsException.java
│   │   ├── repository/
│   │   │   └── MovieRepository.java
│   │   └── service/
│   │       └── MovieService.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/movie/management/
        └── service/
            └── MovieServiceTest.java
```

## Movie Entity

The Movie entity contains the following fields:
- `id` (Long) - Primary key, auto-generated
- `title` (String) - Movie title (required, max 200 characters)
- `genre` (String) - Movie genre (required, max 100 characters)
- `director` (String) - Director name (required, max 100 characters)
- `releaseYear` (Integer) - Release year (required, between 1888-2100)
- `rating` (Double) - Movie rating (optional, between 0.0-10.0)

## API Endpoints

### Get All Movies
```
GET /api/movies
```
Returns a list of all movies.

### Get Movie by ID
```
GET /api/movies/{id}
```
Returns a specific movie by ID.

### Create Movie
```
POST /api/movies
Content-Type: application/json

{
  "title": "The Matrix",
  "genre": "Sci-Fi",
  "director": "Wachowski Brothers",
  "releaseYear": 1999,
  "rating": 8.7
}
```

### Update Movie
```
PUT /api/movies/{id}
Content-Type: application/json

{
  "title": "The Matrix",
  "genre": "Sci-Fi",
  "director": "Wachowski Brothers",
  "releaseYear": 1999,
  "rating": 8.7
}
```

### Delete Movie
```
DELETE /api/movies/{id}
```

### Get Movies by Genre
```
GET /api/movies/genre/{genre}
```

### Get Movies by Director
```
GET /api/movies/director/{director}
```

### Get Movies by Release Year
```
GET /api/movies/year/{year}
```

## Running the Application

1. **Build the project:**
   ```bash
   mvn clean install
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the API:**
   - API Base URL: `http://localhost:8080/api/movies`
   - H2 Console: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:moviedb`
     - Username: `sa`
     - Password: (empty)

## Running Tests

```bash
mvn test
```

## Example API Calls

### Create a Movie
```bash
curl -X POST http://localhost:8080/api/movies \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Inception",
    "genre": "Sci-Fi",
    "director": "Christopher Nolan",
    "releaseYear": 2010,
    "rating": 8.8
  }'
```

### Get All Movies
```bash
curl http://localhost:8080/api/movies
```

### Get Movie by ID
```bash
curl http://localhost:8080/api/movies/1
```

### Update Movie
```bash
curl -X PUT http://localhost:8080/api/movies/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Inception",
    "genre": "Sci-Fi",
    "director": "Christopher Nolan",
    "releaseYear": 2010,
    "rating": 9.0
  }'
```

### Delete Movie
```bash
curl -X DELETE http://localhost:8080/api/movies/1
```

## Exception Handling

The API includes comprehensive exception handling:
- `MovieNotFoundException` (404) - When a movie is not found
- `MovieAlreadyExistsException` (409) - When trying to create/update a movie that already exists
- Validation errors (400) - When request validation fails
- Generic exceptions (500) - For unexpected errors

## Clean Architecture

The project follows clean architecture principles:
- **Entity Layer**: Domain entities
- **Repository Layer**: Data access abstraction
- **Service Layer**: Business logic
- **Controller Layer**: API endpoints and HTTP handling
- **Exception Layer**: Centralized error handling

