package Laba45.Calculator;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;

            while (true) {
                inputLine = in.readLine();
                if (inputLine == null) {
                    System.out.println("Клиент отключен.");
                    break;
                }

                System.out.println("Получено от клиента: " + inputLine);

                if (inputLine.equalsIgnoreCase("exit")) {
                    System.out.println("Клиент завершил соединение.");
                    break;
                }

                String result = processInput(inputLine);

                out.println(result);
            }

        } catch (IOException e) {
            System.out.println("Ошибка соединения с клиентом: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Соединение с клиентом закрыто.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processInput(String input) {
        String[] parts = input.split(" ");
        if (parts.length != 3) {
            return "Ошибка: некорректный формат ввода. Ожидается: число1 оператор число2.";
        }

        try {
            double num1 = Double.parseDouble(parts[0]);
            double num2 = Double.parseDouble(parts[2]);
            String operator = parts[1];

            switch (operator) {
                case "+":
                    return String.valueOf(num1 + num2);
                case "-":
                    return String.valueOf(num1 - num2);
                case "*":
                    return String.valueOf(num1 * num2);
                case "/":
                    if (num2 == 0) {
                        return "Ошибка: деление на ноль.";
                    }
                    return String.valueOf(num1 / num2);
                default:
                    return "Ошибка: неизвестный оператор " + operator;
            }
        } catch (NumberFormatException e) {
            return "Ошибка: неверный формат чисел.";
        }
    }
}