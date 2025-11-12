@echo off
REM Java 21 LTS Build and Run Script for Remote Desktop Application
REM This script provides easy compilation and execution options

setlocal enabledelayedexpansion
cd /d "%~dp0"

echo.
echo =====================================
echo Java 21 LTS Remote Desktop App
echo =====================================
echo.

:menu
echo.
echo Select an option:
echo 1. Compile source code
echo 2. Compile and run Server
echo 3. Compile and run Client
echo 4. Clean compiled files
echo 5. Check Java version
echo 6. Show help
echo 0. Exit
echo.

set /p choice="Enter your choice (0-6): "

if "%choice%"=="1" goto compile
if "%choice%"=="2" goto runserver
if "%choice%"=="3" goto runclient
if "%choice%"=="4" goto clean
if "%choice%"=="5" goto version
if "%choice%"=="6" goto help
if "%choice%"=="0" goto exit
echo Invalid choice. Please try again.
goto menu

:compile
echo.
echo Compiling source code with Java 21...
javac --release 21 -d bin src/Server.java src/Client.java
if %errorlevel% equ 0 (
    echo Compilation successful!
) else (
    echo Compilation failed!
)
goto menu

:runserver
echo.
echo Compiling...
javac --release 21 -d bin src/Server.java src/Client.java
echo.
echo Starting Remote Desktop Server on port 6060...
echo (Press Ctrl+C to stop the server)
echo.
java -cp bin Server
goto menu

:runclient
echo.
echo Compiling...
javac --release 21 -d bin src/Server.java src/Client.java
echo.
echo Starting Remote Desktop Client...
echo (Make sure the Server is running first!)
echo.
java -cp bin Client
goto menu

:clean
echo.
echo Cleaning compiled files...
if exist bin (
    for /r "bin" %%f in (*.class) do del "%%f"
    echo Cleanup complete!
) else (
    echo No compiled files found.
)
goto menu

:version
echo.
java -version
echo.
goto menu

:help
echo.
echo Java 21 LTS Remote Desktop Application
echo =======================================
echo.
echo This application allows you to remotely control a computer's screen
echo using Java sockets and the Robot class.
echo.
echo Components:
echo   - Server: Captures screen and listens for client commands
echo   - Client: Displays remote screen and sends mouse/keyboard input
echo.
echo Requirements:
echo   - Java 21 LTS or later
echo   - Server and Client on same network
echo.
echo Usage:
echo   1. Start the Server on the computer you want to control
echo   2. Note the Server's IP address
echo   3. Run the Client and enter the Server IP
echo   4. Click "Connect" to establish connection
echo.
echo Build Options:
echo   - Maven: mvn clean compile
echo   - Gradle: gradle build
echo   - Javac: javac --release 21 -d bin src/*.java
echo.
goto menu

:exit
echo.
echo Goodbye!
endlocal
exit /b 0
