const fs = require('fs');
const path = require('path');

// Define correct paths
const projectRoot = path.join(__dirname, "remote-desktop-app"); 
const javaDir = path.join(projectRoot, "java"); // Java files go here
const webDir = path.join(projectRoot, "remote-desktop-web"); // Web files go here

// Ensure directories exist
[projectRoot, javaDir, webDir].forEach(dir => {
    if (!fs.existsSync(dir)) {
        fs.mkdirSync(dir, { recursive: true });
        console.log(`Created folder: ${dir}`);
    }
});

// Move Java files
const javaFiles = ["Server.java", "Client.java", "flatlaf-3.2.jar"];
javaFiles.forEach(file => {
    const oldPath = path.join(__dirname, file);
    const newPath = path.join(javaDir, file);

    if (fs.existsSync(oldPath)) {
        fs.renameSync(oldPath, newPath);
        console.log(`Moved ${file} to ${newPath}`);
    }
});

// Move web files
const webFiles = ["server.js", "public"];
webFiles.forEach(file => {
    const oldPath = path.join(__dirname, file);
    const newPath = path.join(webDir, file);

    if (fs.existsSync(oldPath)) {
        fs.renameSync(oldPath, newPath);
        console.log(`Moved ${file} to ${newPath}`);
    }
});

console.log("\n✅ All files are now correctly placed!");
