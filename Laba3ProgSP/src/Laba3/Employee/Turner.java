package Laba3.Employee;

public class Turner extends Worker {
	  public Turner() {
	        super();
	    }

	    public Turner(String fullName, String gender, String birthDate, int workExperience) {
	        super(fullName, gender, birthDate, workExperience);
	    }

	    public Turner(String fullName, String gender, String birthDate, int workExperience, double salary, int workshopNumber, int rank) {
	        super(fullName, gender, birthDate, workExperience, salary, workshopNumber, rank);
	    }

	    @Override
	    public void print() {
	    	System.out.println("Плотник: " + fullName + ", Пол: " + gender +", Дата рождения: " + birthDate+ ", Стаж работы: "+ workExperience + ", Зарплата: "+ salary +", Номер цеха: " + workshopNumber + ", Разряд: " + rank);
	    }

	    @Override
	    public double calculateTaxes() {
	        return salary * 0.12;
	    }
}
