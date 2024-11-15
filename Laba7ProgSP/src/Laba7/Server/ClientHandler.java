package Laba7.Server;

import java.io.*;
import java.net.*;
import java.util.*;

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                CalculatorServer.log("Получено от клиента: " + inputLine);

                if (inputLine.equalsIgnoreCase("exit")) {
                    CalculatorServer.log("Клиент завершил соединение.");
                    break;
                }

                try {
                    String result = evaluateExpression(inputLine);
                    out.println(result);
                    CalculatorServer.log("Результат отправлен клиенту: " + result);

                } catch (IllegalArgumentException e) {
                    out.println("Ошибка: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            CalculatorServer.log("Ошибка соединения с клиентом: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                CalculatorServer.log("Соединение с клиентом закрыто.");
            } catch (IOException e) {
                e.printStackTrace();
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

