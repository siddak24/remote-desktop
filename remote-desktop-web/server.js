const express = require("express");
const { spawn } = require("child_process");
const path = require("path");

const app = express();
const PORT = 5050;
let javaProcess = null;  

const javaDir = "C:\\Users\\ASUS\\OneDrive\\Desktop\\remote desktop app\\java\\bin"; // Use compiled classes directory (bin)
const javaClass = "Server"; // Java class name

// Serve static files (Frontend)
app.use(express.static(path.join(__dirname, "public"))); 
app.use(express.json());

// Start Java Server
function runJavaServer(res) {
    if (javaProcess) {
        return res.json({ success: false, message: "Server already running!" });
    }

    javaProcess = spawn("java", ["-cp", javaDir, javaClass], { cwd: javaDir });

    javaProcess.stdout.on("data", (data) => console.log(`Server: ${data}`));
    javaProcess.stderr.on("data", (data) => console.error(`Error: ${data}`));
    javaProcess.on("close", () => {
        console.log("Java server stopped");
        javaProcess = null;
    });

    res.json({ success: true, message: "Server started successfully!" });
}

// Stop Java Server
function stopJavaServer(res) {
    if (!javaProcess) {
        return res.json({ success: false, message: "No server is running." });
    }
    
    javaProcess.kill();
    javaProcess = null;
    res.json({ success: true, message: "Server stopped successfully!" });
}

// Provide compatibility endpoints used by the Java client
// GET /start is used by the Java client to request server start
app.get("/start", (req, res) => runJavaServer(res));
app.post("/start-server", (req, res) => runJavaServer(res));
app.post("/stop-server", (req, res) => stopJavaServer(res));

// Serve Frontend at http://localhost:5000
app.use(express.static(__dirname)); 

app.listen(PORT, () => {
    console.log(`Server running at http://localhost:${PORT}`);
});
