package Laba45.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class CalculatorClient {

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 8080;               

        try (Socket socket = new Socket(hostname, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            String userInput;

            while (true) {
                System.out.println("Введите выражение в формате: число1 оператор число2:");
                userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println("Завершение работы клиента.");
                    break;
                }

                out.println(userInput);

                String response = in.readLine();
                System.out.println("Ответ от сервера: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}