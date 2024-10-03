package Laba45.Calculator;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// строчка для проверки гита

public class CalculatorServer {
    
    public static void main(String[] args) {
        int port = 8080;
        int maxClients = 10;

        ExecutorService executor = Executors.newFixedThreadPool(maxClients); 

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Подключен новый клиент: " + clientSocket.getInetAddress());

                executor.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}