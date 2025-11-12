import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

public class Server {
    private static ServerSocket serverSocket;
    private static boolean isRunning = true;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(6060); // Remote Desktop Port
            System.out.println("[SERVER] Waiting for client on port 6060...");

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] Client connected: " + clientSocket.getInetAddress());

                new ClientHandler(clientSocket).start();
            }
        } catch (Exception e) {
            System.err.println("[SERVER ERROR] " + e.getMessage());
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private DataOutputStream dataOutputStream;
    private BufferedReader reader;
    private Robot robot;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            robot = new Robot();

            // Start listening for commands
            new Thread(this::handleClientCommands).start();

            // Start sending screen updates
            while (true) {
                BufferedImage screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(screenshot, "jpg", byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                dataOutputStream.writeInt(imageBytes.length);
                dataOutputStream.write(imageBytes);
                dataOutputStream.flush();
                System.out.println("[SERVER] Sent screenshot: " + imageBytes.length + " bytes");

                Thread.sleep(33); // ~30 FPS
            }
        } catch (Exception e) {
            System.err.println("[SERVER] Client disconnected: " + e.getMessage());
        }
    }

    private void handleClientCommands() {
        try {
            String command;
            while ((command = reader.readLine()) != null) {
                String[] parts = command.split(" ");
                if (command.startsWith("MOVE")) {
                    robot.mouseMove(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                } else if (command.equals("CLICK")) {
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                } else if (command.startsWith("KEY")) {
                    int keyCode = Integer.parseInt(parts[1]);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                }
            }
        } catch (IOException e) {
            System.err.println("[SERVER] Error in client commands: " + e.getMessage());
        }
    }
}
