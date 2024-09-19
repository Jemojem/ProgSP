package Laba3.Employee;

public class Mechanic extends Worker {
	public Mechanic() {
        super();
    }

    public Mechanic(String fullName, String gender, String birthDate, int workExperience) {
        super(fullName, gender, birthDate, workExperience);
    }

    public Mechanic(String fullName, String gender, String birthDate, int workExperience, double salary, int workshopNumber, int rank) {
        super(fullName, gender, birthDate, workExperience, salary, workshopNumber, rank);
    }

    @Override
    public void print() {
    	System.out.println("Слесарь: " + fullName + ", Пол: " + gender +", Дата рождения: " + birthDate+ ", Стаж работы: "+ workExperience + ", Зарплата: "+ salary +", Номер цеха: " + workshopNumber + ", Разряд: " + rank);
    }

    @Override
    public double calculateTaxes() {
        return salary * 0.13;
    }
}
