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
                        var calculations = databaseManager.getClientCalculations(clientId);
                        if (calculations.isEmpty()) {
                            out.println("История пуста.");
                        } else {
                            for (String calc : calculations) {
                                out.println(calc);
                            }
                        }
                        out.println("END_HISTORY");
                        CalculatorServer.log("История вычислений отправлена клиенту ID: " + clientId);
                    } catch (Exception e) {
                        CalculatorServer.log("Ошибка при отправке истории для клиента ID " + clientId + ": " + e.getMessage());
                        out.println("Ошибка при загрузке истории.");
                    }
                } else if (inputLine.equals("EXIT")) {
                    CalculatorServer.log("Клиент с ID " + clientId + " завершает сеанс.");
                    break;
                } else {
                    try {
                        String result = evaluateExpression(inputLine);
                        out.println(result);
                        CalculatorServer.log("Результат вычисления отправлен клиенту ID " + clientId + ": " + inputLine + " = " + result);
                    } catch (IllegalArgumentException e) {
                        out.println("Ошибка: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            CalculatorServer.log("Ошибка ввода/вывода для клиента ID " + clientId + ": " + e.getMessage());
        } finally {
            CalculatorServer.log("Удаление записей клиента ID: " + clientId);
            databaseManager.clearClientData(clientId);
            try {
                clientSocket.close();
                CalculatorServer.log("Соединение с клиентом ID " + clientId + " закрыто.");
            } catch (IOException e) {
                CalculatorServer.log("Ошибка закрытия соединения для клиента ID " + clientId + ": " + e.getMessage());
            }
        }
    }

    private String evaluateExpression(String expression) {
        try {
            List<String> rpn = toReversePolishNotation(expression);
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
