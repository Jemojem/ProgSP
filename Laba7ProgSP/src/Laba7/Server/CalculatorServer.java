package Laba7.Server;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class CalculatorServer {

    private static JTextArea logArea;

    public static void main(String[] args) {
        JFrame serverFrame = new JFrame("Calculator Server");
        serverFrame.setSize(400, 300);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logArea = new JTextArea();
        logArea.setEditable(false);
        serverFrame.add(new JScrollPane(logArea), BorderLayout.CENTER);
        
        serverFrame.setVisible(true);

        int port = 8080;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log("Сервер запущен на порту " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                log("Подключен новый клиент: " + clientSocket.getInetAddress());

                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }
}

