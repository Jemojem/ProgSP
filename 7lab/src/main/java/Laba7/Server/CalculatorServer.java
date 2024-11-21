package Laba7.Server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer {
    private static JTextArea logArea;
    private static DatabaseManager databaseManager;
    private static boolean isRunning = true;
    public static void main(String[] args) {
        log("Инициализация подключения к базе данных...");
        databaseManager = new DatabaseManager();
        databaseManager.createTables();
        log("Таблицы базы данных проверены/созданы.");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log("Сервер завершается. Очистка таблиц...");
            databaseManager.clearAllTables();
            log("Все таблицы очищены. Сервер завершил работу.");
        }));
        JFrame serverFrame = new JFrame("Calculator Server");
        serverFrame.setSize(400, 300);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logArea = new JTextArea();
        logArea.setEditable(false);
        serverFrame.add(new JScrollPane(logArea), BorderLayout.CENTER);

        JButton showClientsButton = new JButton("Показать клиентов");
        showClientsButton.addActionListener(e -> showClientsInfo());
        serverFrame.add(showClientsButton, BorderLayout.SOUTH);

        serverFrame.setVisible(true);
        int port = 8080;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log("Сервер запущен на порту " + port);

            while (isRunning) {
                log("Ожидание новых клиентов...");
                Socket clientSocket = serverSocket.accept();
                String clientAddress = clientSocket.getInetAddress().getHostAddress();
                log("Новый клиент подключён: " + clientAddress);

                int clientId = databaseManager.addClient(clientAddress);
                log("Клиент добавлен в базу с ID: " + clientId);

                Thread clientThread = new Thread(new ClientHandler(clientSocket, clientId, databaseManager));
                clientThread.start();
                log("Создан поток для клиента с ID: " + clientId);
            }
        } catch (IOException e) {
            log("Ошибка работы сервера: " + e.getMessage());
        }
    }

    public static void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
        System.out.println("[LOG] " + message);
    }

    private static void showClientsInfo() {
        try {
            var results = databaseManager.getAllClients();
            StringBuilder info = new StringBuilder("Список клиентов и их вычислений:\n");

            while (results.next()) {
                String clientId = results.getString("id");
                String ipAddress = results.getString("ip_address");
                String calculation = results.getString("calculation");
                info.append("ID: ").append(clientId)
                    .append(", IP: ").append(ipAddress)
                    .append(", Вычисление: ").append(calculation == null ? "Нет данных" : calculation)
                    .append("\n");
            }
            log("Список клиентов:\n" + info);
            JOptionPane.showMessageDialog(null, info.toString(), "Информация о клиентах", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            log("Ошибка получения информации о клиентах: " + e.getMessage());
        }
    }
}

