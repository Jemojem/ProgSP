package Laba7.Server;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/calculator_db?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "ЗМЩлшкпшы789";

    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Соединение с базой данных установлено.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных: " + e.getMessage(), e);
        }
    }

    public void createTables() {
        String createClientsTable = """
            CREATE TABLE IF NOT EXISTS clients (
                id INT AUTO_INCREMENT PRIMARY KEY,
                ip_address VARCHAR(45),
                connect_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;

        String createCalculationsTable = """
            CREATE TABLE IF NOT EXISTS calculations (
                id INT AUTO_INCREMENT PRIMARY KEY,
                client_id INT,
                calculation TEXT,
                FOREIGN KEY (client_id) REFERENCES clients(id)
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createClientsTable);
            stmt.execute(createCalculationsTable);
            System.out.println("Таблицы проверены/созданы.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблиц: " + e.getMessage(), e);
        }
    }

    public int addClient(String ipAddress) {
        String insertClient = "INSERT INTO clients (ip_address) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertClient, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ipAddress);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1); // Возвращаем ID клиента
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления клиента: " + e.getMessage(), e);
        }
        return -1;
    }

    public void saveCalculation(int clientId, String calculation) {
        String insertCalculation = "INSERT INTO calculations (client_id, calculation) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertCalculation)) {
            stmt.setInt(1, clientId);
            stmt.setString(2, calculation);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения вычисления: " + e.getMessage(), e);
        }
    }

    public List<String> getClientCalculations(int clientId) {
        String query = "SELECT calculation FROM calculations WHERE client_id = ?";
        List<String> calculations = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    calculations.add(rs.getString("calculation"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения вычислений: " + e.getMessage(), e);
        }
        return calculations;
    }

    public ResultSet getAllClients() {
        String query = """
            SELECT c.id, c.ip_address, cal.calculation 
            FROM clients c
            LEFT JOIN calculations cal ON c.id = cal.client_id;
        """;
        try {
            return connection.createStatement().executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения данных клиентов: " + e.getMessage(), e);
        }
    }

    public void clearAllTables() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM calculations");
            stmt.executeUpdate("DELETE FROM clients");
            stmt.executeUpdate("ALTER TABLE calculations AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE clients AUTO_INCREMENT = 1");
            
            System.out.println("Все таблицы очищены и автоинкременты сброшены.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка очистки таблиц: " + e.getMessage(), e);
        }
    }
    public void clearClientData(int clientId) {
        String deleteCalculations = "DELETE FROM calculations WHERE client_id = ?";
        String deleteClient = "DELETE FROM clients WHERE id = ?";
        try (PreparedStatement stmtCalc = connection.prepareStatement(deleteCalculations);
             PreparedStatement stmtClient = connection.prepareStatement(deleteClient)) {
            stmtCalc.setInt(1, clientId);
            stmtCalc.executeUpdate();
            stmtClient.setInt(1, clientId);
            stmtClient.executeUpdate();
            System.out.println("Записи клиента ID " + clientId + " удалены.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления данных клиента ID " + clientId + ": " + e.getMessage(), e);
        }
    }
}