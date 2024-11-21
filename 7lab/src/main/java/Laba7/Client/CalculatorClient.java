package Laba7.Client;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class CalculatorClient {
    private JFrame frame;
    private JTextField inputField;
    private JButton[] numberButtons;
    private JButton[] operatorButtons;
    private JButton equalsButton;
    private JButton clearButton;
    private JButton saveButton;
    private JButton historyButton;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private boolean operatorPressed = false;

    public CalculatorClient(String hostname, int port) {
        try {
            socket = new Socket(hostname, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            showError("Ошибка подключения к серверу: " + e.getMessage());
        }

        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Калькулятор");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 500);
        frame.setLayout(new BorderLayout());
        inputField = new JTextField();
        inputField.setEditable(false);
        inputField.setFont(new Font("Arial", Font.BOLD, 24));
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        frame.add(inputField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5));

        numberButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            int number = i;
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].setFont(new Font("Arial", Font.BOLD, 20));
            numberButtons[i].addActionListener(e -> appendNumber(number));
        }

        String[] operators = {"+", "*", "/", "-"};
        operatorButtons = new JButton[operators.length];
        for (int i = 0; i < operators.length; i++) {
            String operator = operators[i];
            operatorButtons[i] = new JButton(operator);
            operatorButtons[i].setFont(new Font("Arial", Font.BOLD, 20));
            operatorButtons[i].addActionListener(e -> appendOperator(operator));
        }

        equalsButton = new JButton("=");
        equalsButton.setFont(new Font("Arial", Font.BOLD, 20));
        equalsButton.addActionListener(e -> sendExpression());

        clearButton = new JButton("C");
        clearButton.setFont(new Font("Arial", Font.BOLD, 20));
        clearButton.addActionListener(e -> clearInput());
        
        saveButton = new JButton("Сохранить");
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));
        saveButton.addActionListener(e -> saveCalculation());
        
        historyButton = new JButton("История");
        historyButton.setFont(new Font("Arial", Font.BOLD, 20));
        historyButton.addActionListener(e -> loadHistory());

        buttonPanel.add(numberButtons[7]);
        buttonPanel.add(numberButtons[8]);
        buttonPanel.add(numberButtons[9]);
        buttonPanel.add(operatorButtons[0]);

        buttonPanel.add(numberButtons[4]);
        buttonPanel.add(numberButtons[5]);
        buttonPanel.add(numberButtons[6]);
        buttonPanel.add(operatorButtons[1]);

        buttonPanel.add(numberButtons[1]);
        buttonPanel.add(numberButtons[2]);
        buttonPanel.add(numberButtons[3]);
        buttonPanel.add(operatorButtons[2]);

        buttonPanel.add(numberButtons[0]);
        buttonPanel.add(clearButton);
        buttonPanel.add(equalsButton);
        buttonPanel.add(operatorButtons[3]);
        buttonPanel.add(saveButton);
        buttonPanel.add(historyButton);

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        
    }

    private void appendNumber(int number) {
        if (operatorPressed) {
            inputField.setText(inputField.getText() + "");
            operatorPressed = false;
        }
        inputField.setText(inputField.getText() + number);
    }

    private void appendOperator(String operator) {
        if (!operatorPressed && !inputField.getText().isEmpty()) {
            inputField.setText(inputField.getText() + " " + operator + " ");
            operatorPressed = true;
        }
    }

    private void clearInput() {
        inputField.setText("");
        operatorPressed = false;
    }

    private void sendExpression() {
        String expression = inputField.getText().trim();
        if (expression.isEmpty()) {
            showError("Введите выражение!");
            return;
        }
        try {
            out.println(expression);
            String response = in.readLine();
            if (response != null) {
                inputField.setText(response);
            } else {
                showError("Сервер не отправил ответа.");
            }
        } catch (IOException e) {
            showError("Ошибка при отправке данных на сервер: " + e.getMessage());
        }

        operatorPressed = false;
    }
    
    private void saveCalculation() {
        String expression = inputField.getText().trim();
        if (expression.isEmpty()) {
            showError("Введите выражение для сохранения!");
            return;
        }
        try {
            out.println("SAVE:" + expression);
            String response = in.readLine();
            if (response != null) {
                JOptionPane.showMessageDialog(frame, response, "Сохранение", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            showError("Ошибка сохранения: " + e.getMessage());
        }
    }

    private void loadHistory() {
        try {
            out.println("HISTORY");
            StringBuilder history = new StringBuilder("История вычислений:\n");
            String line;
            while (!(line = in.readLine()).equals("END_HISTORY")) {
                history.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(frame, history.toString(), "История", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showError("Ошибка загрузки истории: " + e.getMessage());
        }
    }


    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorClient("localhost", 8080));
    }
}
