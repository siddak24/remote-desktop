document.getElementById("startServer").addEventListener("click", async () => {
    const response = await fetch("/start-server", { method: "POST" });
    const data = await response.json();
    
    document.getElementById("status").innerText = data.message;
    
    if (data.success) {
        startServer();
    } else {
        showError();
    }
});

document.getElementById("stopServer").addEventListener("click", async () => {
    const response = await fetch("/stop-server", { method: "POST" });
    const data = await response.json();
    
    document.getElementById("status").innerText = data.message;
    stopServer();
});

const container = document.querySelector(".container");
const statusText = document.querySelector(".status");

function startServer() {
    container.classList.add("running");
    container.classList.remove("error");
    statusText.classList.add("running");
    statusText.classList.remove("error");
    statusText.innerText = "Server Running!";
    
    // Add green glow effect
    container.style.boxShadow = "0 0 20px 5px rgba(0, 255, 0, 0.8)";
}

function stopServer() {
    container.classList.add("error");
    container.classList.remove("running");
    statusText.classList.add("error");
    statusText.classList.remove("running");
    statusText.innerText = "Server Stopped!";
    
    // Add red glow effect
    container.style.boxShadow = "0 0 20px 5px rgba(255, 0, 0, 0.8)";
}

function showError() {
    container.classList.add("error");
    container.classList.remove("running");
    statusText.classList.add("error");
    statusText.classList.remove("running");
    statusText.innerText = "Error Occurred!";
    
    // Add red glow effect for errors
    container.style.boxShadow = "0 0 20px 5px rgba(255, 0, 0, 0.8)";
}
