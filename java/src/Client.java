import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client {
    private static Socket socket;
    private static DataInputStream dataInputStream;
    private static PrintWriter writer;
    private static JLabel screenLabel;
    private static JTextField ipField;
    private static JLabel statusLabel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Remote Desktop Client");
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        ipField = new JTextField("Enter Server IP", 15);
        JButton connectButton = new JButton("Connect");
        statusLabel = new JLabel("Not Connected", SwingConstants.CENTER);

        topPanel.add(ipField);
        topPanel.add(connectButton);

        screenLabel = new JLabel();
        JScrollPane scrollPane = new JScrollPane(screenLabel);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(statusLabel, BorderLayout.SOUTH);

        frame.setVisible(true);

        connectButton.addActionListener(e -> new Thread(() -> connectToServer(ipField.getText())).start());
    }

    public static void connectToServer(String serverIP) {
        try {
            // Start the server via HTTP request
            sendHttpRequest(serverIP);

            // Connect to Remote Desktop Server
            socket = new Socket(serverIP, 6060);
            statusLabel.setText("Connected!");
            dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            screenLabel.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent e) {
                    writer.println("MOVE " + e.getX() + " " + e.getY());
                }
            });

            screenLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    writer.println("CLICK");
                }
            });

            screenLabel.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    writer.println("KEY " + e.getKeyCode());
                }
            });

            while (true) {
                int imageSize = dataInputStream.readInt();
                byte[] imageBytes = new byte[imageSize];
                dataInputStream.readFully(imageBytes);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                BufferedImage image = ImageIO.read(byteArrayInputStream);

                screenLabel.setIcon(new ImageIcon(image));
                screenLabel.repaint();
                System.out.println("[CLIENT] Received image.");
            }
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private static void sendHttpRequest(String serverIP) {
        try {
            URI uri = new URI("http://" + serverIP + ":5050/start");
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.getResponseCode(); 
            connection.disconnect();
            System.out.println("[CLIENT] Server started via HTTP request.");
        } catch (Exception e) {
            System.err.println("[CLIENT] HTTP request failed: " + e.getMessage());
        }
    }
}
