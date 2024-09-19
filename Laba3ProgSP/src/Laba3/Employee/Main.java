package Laba3.Employee;

import java.util.ArrayList;
import java.util.List;

public class Main {
	  public static void main(String[] args) {

		  Worker turner = new Turner("Иван Иванов", "Мужской", "01-01-1980", 10, 50000.0, 3, 5);
	        Worker mechanic = new Mechanic("Алексей Петров", "Мужской", "15-05-1985", 12, 55000.0, 4, 4);
	        FactoryManager factoryManager = new FactoryManager("Мария Смирнова", "Женский", "20-03-1975", 15, 80000.0, "Цех 1", 25);
	        FactoryDirector director = new FactoryDirector("Сергей Александров", "Мужской", "10-10-1970", 20, 150000.0, "02-04-2020");

	        List<Object> employeesObj = new ArrayList<>();
	        employeesObj.add(turner);
	        employeesObj.add(mechanic);
	        employeesObj.add(factoryManager);
	        employeesObj.add(director);
	        
	        List<Employee> employees = new ArrayList<>();
	        employees.add(turner);
	        employees.add(mechanic);
	        employees.add(factoryManager);
	        employees.add(director);

	        System.out.println("Вывод данных о всех сотрудниках:");
	        for (Object employee : employeesObj) {
	            employee.print();
	        }

	        System.out.println("\nНалоги для каждого сотрудника:");
	        for (Employee employee : employees) {
	            System.out.println(employee + " - Налоги: " + employee.calculateTaxes());
	        }

	        Worker turner2 = new Turner("Иван Иванов", "Мужской", "01-01-1980", 10, 50000.0, 3, 5);

	        System.out.println("\nПроверка equals и hashCode:");
	        System.out.println("turner.equals(turner2): " + turner.equals(turner2)); // true
	        System.out.println("hashCode turner: " + turner.hashCode());
	        System.out.println("hashCode turner2: " + turner2.hashCode());

	        System.out.println("\nПроверка метода toString для каждого сотрудника:");
	        for (Object employee : employeesObj) {
	            System.out.println(employee.toString());
	        }
	  }
}
