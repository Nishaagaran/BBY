# Quick Start Guide

## How to Run the Movie Management API

### Option 1: Using the Run Script (Recommended - Automatically clears port 8080)

**On Windows (PowerShell):**
```powershell
.\run.ps1
```

This script will:
- Check for processes on port 8080
- Kill any processes using port 8080
- Verify the port is free
- Start the Spring Boot application

### Option 2: Clear Port 8080 Manually, Then Run

1. **Kill processes on port 8080:**
   ```powershell
   .\kill-port-8080.ps1
   ```

2. **Run the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

### Option 3: Using Maven Directly

1. **Navigate to the project directory:**
   ```bash
   cd C:\BBY
   ```

2. **Manually kill port 8080 (if needed):**
   ```powershell
   Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess | ForEach-Object { Stop-Process -Id $_ -Force }
   ```

3. **Run the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Option 2: Build and Run JAR

1. **Build the project:**
   ```bash
   mvn clean package
   ```

2. **Run the JAR file:**
   ```bash
   java -jar target/movie-management-api-1.0.0.jar
   ```

### Option 3: Using IDE (IntelliJ IDEA / Eclipse)

1. Open the project in your IDE
2. Right-click on `MovieManagementApplication.java`
3. Select "Run" or "Run 'MovieManagementApplication'"

## Verify the Application is Running

Once started, you should see output like:
```
Started MovieManagementApplication in X.XXX seconds
```

## Access Points

- **API Base URL:** `http://localhost:8080/api/movies`
- **H2 Console:** `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:moviedb`
  - Username: `sa`
  - Password: (leave empty)

## Quick Test

Test the API by creating a movie:
```bash
curl -X POST http://localhost:8080/api/movies -H "Content-Type: application/json" -d "{\"title\":\"Inception\",\"genre\":\"Sci-Fi\",\"director\":\"Christopher Nolan\",\"releaseYear\":2010,\"rating\":8.8}"
```

Or use a REST client like Postman, Insomnia, or your browser (for GET requests).

