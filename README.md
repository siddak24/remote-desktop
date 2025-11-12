# 🖥️ Remote Desktop Application

A lightweight, Java-based remote desktop application that allows you to control a computer's screen remotely over the network. Built with **Java 21 LTS** for performance and modern features.

## ✨ Features

- **Live Screen Streaming** - Real-time screen capture and display (~30 FPS)
- **Mouse Control** - Move and click remotely
- **Keyboard Control** - Send keystrokes to remote machine
- **Network Based** - Uses Java sockets for reliable communication
- **Cross-Platform** - Runs on Windows, Linux, and macOS (Java 21+)
- **Java 21 LTS** - Latest long-term support Java version (supported until Sept 2028)

## 📋 Project Structure

```
remote-desktop-app/
├── java/                              # Java Remote Desktop Application
│   ├── src/
│   │   ├── Client.java               # Remote Desktop Client (GUI)
│   │   └── Server.java               # Remote Desktop Server (Screen Capture)
│   ├── bin/                          # Compiled class files
│   ├── pom.xml                       # Maven build configuration
│   ├── build.gradle                  # Gradle build configuration
│   ├── build.bat                     # Windows Batch automation script
│   ├── build.ps1                     # PowerShell automation script
│   ├── QUICK_START.md                # Quick reference guide
│   ├── README.md                     # Detailed Java project guide
│   └── JAVA21_UPGRADE.md             # Java 21 features documentation
├── remote-desktop-web/               # Web Helper (Node.js)
│   ├── server.js                     # Express web server (port 5050)
│   ├── package.json                  # Node.js dependencies
│   ├── public/                       # Frontend files
│   └── node_modules/                 # Dependencies (npm install)
├── legal/                            # JavaFX license information
├── README.md                         # This file
└── .gitignore                        # Git ignore rules
```

## 🚀 Quick Start

### Prerequisites
- **Java 21 LTS** or later ([Download](https://www.oracle.com/java/technologies/javase/21-0-1-relnotes.html))
- **Network connection** (same network or VPN for remote access)

### Fastest Method (No Build Tools Needed)

**Terminal 1 - Start Server:**
```bash
cd java
javac --release 21 -d bin src/Server.java src/Client.java
java -cp bin Server
```

**Terminal 2 - Start Client:**
```bash
cd java
java -cp bin Client
```

**In the Client GUI:**
1. Enter Server IP: `127.0.0.1` (localhost) or your server's IP
2. Click "Connect"
3. Control the remote desktop with mouse and keyboard

### Alternative: Using Maven
```bash
cd java
mvn clean compile
mvn exec:java -Dexec.mainClass="Server"     # Terminal 1
mvn exec:java -Dexec.mainClass="Client"     # Terminal 2
```

### Alternative: Using Gradle
```bash
cd java
./gradlew build
./gradlew run                                 # Terminal 1
./gradlew run -Dorg.gradle.java.home=...    # Terminal 2
```

### Alternative: Using Build Scripts (Windows)
```powershell
cd java
.\build.bat              # Menu-driven compilation and execution
# or
.\build.ps1
```

## 🏗️ Architecture

```
┌─────────────┐                        ┌──────────────┐
│   Client    │  ◄──Socket (6060)──►  │   Server     │
│  (GUI)      │                        │  (Capture)   │
└─────────────┘                        └──────────────┘
     ▲
     │ HTTP GET :5050/start
     │ (Optional Web Helper)
     ▼
┌──────────────────┐
│  Node.js Server  │
│  (Express)       │
└──────────────────┘
```

### Communication Flow
1. **Client** sends HTTP GET to `http://<server>:5050/start` (optional, starts Java Server)
2. **Client** connects to **Server** on port 6060 via TCP socket
3. **Server** captures screen continuously and sends JPEG images
4. **Client** displays screenshots and sends mouse/keyboard commands back

## 📝 Usage

### Running Server Only
```bash
cd java
java -cp bin Server
```
- Server listens on port 6060
- Waits for client connections
- Captures screen at ~30 FPS

### Running Client Only
```bash
cd java
java -cp bin Client
```
- GUI window appears
- Input server IP address
- Click "Connect" to establish connection
- Control remote desktop

### Network Configuration
- **Same Computer**: Use `127.0.0.1` or `localhost`
- **Local Network**: Use server's local IP (e.g., `192.168.1.100`)
- **Remote (VPN)**: Use VPN IP or public IP with port forwarding

## 🔧 Configuration

### Ports Used
- **6060** - Java Server (Remote Desktop Protocol)
- **5050** - Web Helper Server (HTTP, optional)

### Performance Tuning
- **Screenshot Delay**: Line 58 in `Server.java` → `Thread.sleep(33)` (30 FPS)
- **Image Quality**: Change `"jpg"` to `"png"` for better quality (larger file size)
- **Compression**: Modify JPEG quality in `ImageIO.write()`

## 📚 Documentation

| File | Purpose |
|------|---------|
| `java/QUICK_START.md` | Quick reference for running the app |
| `java/README.md` | Detailed Java project documentation |
| `java/JAVA21_UPGRADE.md` | Java 21 features and improvements |
| `java/UPGRADE_REPORT.md` | Technical upgrade details |

## 🎯 Java 21 LTS Features Used

✅ **Modern APIs** - Using current Java standards
✅ **Virtual Threads Ready** - Can be upgraded for better scalability
✅ **Zero Deprecations** - No deprecated API usage
✅ **Long-Term Support** - Supported until September 2028

## 🚨 Troubleshooting

| Problem | Solution |
|---------|----------|
| "Connection refused" | Ensure Server is running on same/reachable network |
| "Port 6060 already in use" | Kill existing Java process: `taskkill /IM java.exe /F` |
| "No screenshots showing" | Check firewall allows port 6060 |
| "Compilation error" | Ensure Java 21+ is installed: `java -version` |

## 🔐 Security Considerations

⚠️ **This app does NOT include encryption or authentication.** For production use, consider:

- ✅ Add TLS/SSL encryption
- ✅ Implement username/password authentication
- ✅ Use VPN for remote connections
- ✅ Restrict to trusted networks only
- ✅ Add activity logging
- ✅ Implement connection timeout/idle disconnect

## 🛠️ Customization

### Change Default Port
**Server.java (line 44):**
```java
ServerSocket serverSocket = new ServerSocket(6060);  // Change 6060
```

**Client.java (line 63):**
```java
socket = new Socket(serverIP, 6060);  // Change 6060
```

### Change Screenshot Quality
**Server.java (line 53):**
```java
ImageIO.write(screenshot, "jpg", byteArrayOutputStream);  // Change format
```

### Change Frame Rate
**Server.java (line 58):**
```java
Thread.sleep(33); // 30 FPS (change 33 for different rate)
```

## 📦 Build Options

### Maven
```bash
mvn clean compile      # Compile
mvn package           # Create JAR
mvn exec:java ...     # Run
```

### Gradle
```bash
./gradlew build       # Compile and package
./gradlew run         # Run
```

### Javac (No Build Tools)
```bash
javac --release 21 -d bin src/*.java  # Compile
java -cp bin Server                   # Run
```

## 📊 System Requirements

| Requirement | Minimum | Recommended |
|-------------|---------|-------------|
| Java | 21 LTS | 21+ LTS |
| RAM | 256 MB | 512 MB+ |
| Network | 1 Mbps | 10 Mbps+ |
| CPU | Any | Multi-core |

## 🤝 Contributing

To improve this project:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/improvement`)
3. Commit changes (`git commit -am 'Add improvement'`)
4. Push to branch (`git push origin feature/improvement`)
5. Create Pull Request

## 📜 License

This project is provided as-is for educational and personal use.

## ✉️ Contact & Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Check existing documentation in `java/` directory
- Review `QUICK_START.md` for common problems

## 🎓 Learning Resources

- [Java 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [Java Networking](https://docs.oracle.com/javase/tutorial/networking/)
- [Java Swing GUI](https://docs.oracle.com/javase/tutorial/uiswing/)
- [Virtual Threads (Java 19+)](https://openjdk.org/projects/loom/)

## 🎉 Status

✅ **Production Ready** - Java 21 LTS Upgraded  
✅ **Fully Documented** - Comprehensive guides included  
✅ **Version Controlled** - All code on GitHub  
✅ **Cross-Platform** - Windows, Linux, macOS support

---

**Last Updated:** November 12, 2025  
**Java Version:** 21 LTS  
**Status:** Active & Maintained

**Happy Remote Desktop-ing! 🚀**
