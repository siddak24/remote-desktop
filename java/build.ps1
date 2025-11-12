# Java 21 LTS Build and Run Script for Remote Desktop Application
# PowerShell version for Windows

function Show-Menu {
    Write-Host ""
    Write-Host "=====================================" -ForegroundColor Cyan
    Write-Host "Java 21 LTS Remote Desktop App" -ForegroundColor Cyan
    Write-Host "=====================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "1. Compile source code"
    Write-Host "2. Compile and run Server"
    Write-Host "3. Compile and run Client"
    Write-Host "4. Clean compiled files"
    Write-Host "5. Check Java version"
    Write-Host "6. Show help"
    Write-Host "0. Exit"
    Write-Host ""
}

function Compile-Project {
    Write-Host ""
    Write-Host "Compiling source code with Java 21..." -ForegroundColor Yellow
    javac --release 21 -d bin src/Server.java src/Client.java
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Compilation successful!" -ForegroundColor Green
    } else {
        Write-Host "❌ Compilation failed!" -ForegroundColor Red
    }
}

function Run-Server {
    Write-Host ""
    Compile-Project
    Write-Host ""
    Write-Host "Starting Remote Desktop Server on port 6060..." -ForegroundColor Yellow
    Write-Host "(Press Ctrl+C to stop the server)" -ForegroundColor Gray
    Write-Host ""
    java -cp bin Server
}

function Run-Client {
    Write-Host ""
    Compile-Project
    Write-Host ""
    Write-Host "Starting Remote Desktop Client..." -ForegroundColor Yellow
    Write-Host "(Make sure the Server is running first!)" -ForegroundColor Gray
    Write-Host ""
    java -cp bin Client
}

function Clean-Project {
    Write-Host ""
    Write-Host "Cleaning compiled files..." -ForegroundColor Yellow
    if (Test-Path "bin") {
        Get-ChildItem -Path "bin" -Filter "*.class" -Recurse | Remove-Item -Force
        Write-Host "✅ Cleanup complete!" -ForegroundColor Green
    } else {
        Write-Host "No compiled files found." -ForegroundColor Gray
    }
}

function Show-Version {
    Write-Host ""
    java -version
    Write-Host ""
}

function Show-Help {
    Write-Host ""
    Write-Host "Java 21 LTS Remote Desktop Application" -ForegroundColor Cyan
    Write-Host "=======================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "This application allows you to remotely control a computer's screen"
    Write-Host "using Java sockets and the Robot class."
    Write-Host ""
    Write-Host "Components:" -ForegroundColor Yellow
    Write-Host "  - Server: Captures screen and listens for client commands"
    Write-Host "  - Client: Displays remote screen and sends mouse/keyboard input"
    Write-Host ""
    Write-Host "Requirements:" -ForegroundColor Yellow
    Write-Host "  - Java 21 LTS or later"
    Write-Host "  - Server and Client on same network"
    Write-Host ""
    Write-Host "Usage:" -ForegroundColor Yellow
    Write-Host "  1. Start the Server on the computer you want to control"
    Write-Host "  2. Note the Server's IP address"
    Write-Host "  3. Run the Client and enter the Server IP"
    Write-Host "  4. Click 'Connect' to establish connection"
    Write-Host ""
    Write-Host "Build Options:" -ForegroundColor Yellow
    Write-Host "  - Maven: mvn clean compile"
    Write-Host "  - Gradle: gradle build"
    Write-Host "  - Javac: javac --release 21 -d bin src/*.java"
    Write-Host ""
}

# Main loop
$running = $true
while ($running) {
    Show-Menu
    $choice = Read-Host "Enter your choice (0-6)"
    
    switch ($choice) {
        "1" { Compile-Project }
        "2" { Run-Server }
        "3" { Run-Client }
        "4" { Clean-Project }
        "5" { Show-Version }
        "6" { Show-Help }
        "0" { 
            Write-Host ""
            Write-Host "Goodbye!" -ForegroundColor Green
            $running = $false 
        }
        default { 
            Write-Host "Invalid choice. Please try again." -ForegroundColor Red 
        }
    }
}
