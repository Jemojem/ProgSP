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
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

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
				
				String[] parts = inputLine.split(" ");
				if (parts.length != 3) {
					out.println( "Ошибка: некорректный формат ввода. Ожидается: число1 оператор число2.");
					continue;
				}

				try {
					double num1 = Double.parseDouble(parts[0]);
					double num2 = Double.parseDouble(parts[2]);
					String operator = parts[1];

					switch (operator) {
					case "+":
						out.println(String.valueOf(num1 + num2));
						break;
					case "-":
						out.println( String.valueOf(num1 - num2));
						break;
					case "*":
						out.println(String.valueOf(num1 * num2));
						break;
					case "/":
						if (num2 == 0) {
							out.println("Ошибка: деление на ноль.");
						}
						out.println(String.valueOf(num1 / num2));
					default:
						out.println( "Ошибка: неизвестный оператор " + operator);
					}
				} catch (NumberFormatException e) {
					out.println( "Ошибка: неверный формат чисел.");
				}
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
}