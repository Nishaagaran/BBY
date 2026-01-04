# Script to kill processes on port 8080 and start Spring Boot application

Write-Host "Checking for processes on port 8080..." -ForegroundColor Yellow

# Find processes using port 8080
$processes = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess -Unique

if ($processes) {
    Write-Host "Found processes on port 8080: $($processes -join ', ')" -ForegroundColor Red
    foreach ($pid in $processes) {
        try {
            $proc = Get-Process -Id $pid -ErrorAction SilentlyContinue
            if ($proc) {
                Write-Host "Killing process: $($proc.ProcessName) (PID: $pid)" -ForegroundColor Yellow
                Stop-Process -Id $pid -Force -ErrorAction Stop
                Write-Host "Process $pid killed successfully" -ForegroundColor Green
            }
        } catch {
            Write-Host "Could not kill process $pid : $_" -ForegroundColor Red
        }
    }
    # Wait a moment for port to be released
    Start-Sleep -Seconds 2
} else {
    Write-Host "No processes found on port 8080" -ForegroundColor Green
}

# Verify port is free
$stillRunning = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if ($stillRunning) {
    Write-Host "WARNING: Port 8080 is still in use!" -ForegroundColor Red
    Write-Host "Please manually kill the process or choose a different port." -ForegroundColor Yellow
    exit 1
} else {
    Write-Host "Port 8080 is free. Starting Spring Boot application..." -ForegroundColor Green
    Write-Host ""
    # Start the Spring Boot application
    mvn spring-boot:run
}

