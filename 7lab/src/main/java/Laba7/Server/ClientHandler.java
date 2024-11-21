package Laba7.Server;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;


class ClientHandler implements Runnable {
    private Socket clientSocket;
    private int clientId;
    private DatabaseManager databaseManager;

    public ClientHandler(Socket socket, int clientId, DatabaseManager dbManager) {
        this.clientSocket = socket;
        this.clientId = clientId;
        this.databaseManager = dbManager;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("SAVE:")) {
                    String calculation = inputLine.substring(5);
                    databaseManager.saveCalculation(clientId, calculation);
                    out.println("Calculation saved.");
                } else if (inputLine.equals("HISTORY")) {
                    try {
                        var results = databaseManager.getClientCalculations(clientId);
                        while (results.next()) {
                            out.println(results.getString("calculation"));
                        }
                        out.println("END_HISTORY");
                    } catch (SQLException e) {
                        CalculatorServer.log("Ошибка получения истории: " + e.getMessage());
                        out.println("Ошибка при загрузке истории.");
                    }
                } else {
                    // Обычное вычисление
                    String result = evaluateExpression(inputLine);
                    out.println(result);
                    databaseManager.saveCalculation(clientId, inputLine + " = " + result);
                }
            }
        }
            }
    private String evaluateExpression(String expression) {
        try {
            // Преобразование выражения в обратную польскую запись
            List<String> rpn = toReversePolishNotation(expression);

            // Вычисление результата из RPN
            double result = calculateRPN(rpn);

            return String.valueOf(result);
        } catch (Exception e) {
            throw new IllegalArgumentException("Некорректное выражение: " + expression);
        }
    }

    private List<String> toReversePolishNotation(String expression) {
        String[] tokens = expression.split(" ");
        Stack<String> operators = new Stack<>();
        List<String> output = new ArrayList<>();

        Map<String, Integer> precedence = Map.of(
                "+", 1, "-", 1,
                "*", 2, "/", 2
        );

        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if (precedence.containsKey(token)) {
                while (!operators.isEmpty() && precedence.containsKey(operators.peek())
                        && precedence.get(operators.peek()) >= precedence.get(token)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else {
                throw new IllegalArgumentException("Неверный оператор: " + token);
            }
        }

        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }

        return output;
    }

    private double calculateRPN(List<String> rpn) {
        Stack<Double> stack = new Stack<>();

        for (String token : rpn) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();

                switch (token) {
                    case "+" -> stack.push(a + b);
                    case "-" -> stack.push(a - b);
                    case "*" -> stack.push(a * b);
                    case "/" -> {
                        if (b == 0) {
                            throw new ArithmeticException("Деление на ноль.");
                        }
                        stack.push(a / b);
                    }
                    default -> throw new IllegalArgumentException("Неверный оператор: " + token);
                }
            }
        }

        return stack.pop();
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}