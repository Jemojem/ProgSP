package Laba3.Employee;
import java.util.*;

public class FactoryManager implements Object, Employee {

	private String fullName;
    private String gender;
    private String birthDate;
    private int workExperience;
    private double salary;
    private String department; 
    private int subordinateCount;

    public FactoryManager() {
        this.fullName = "Unknown";
        this.gender = "Unknown";
        this.birthDate = "Unknown";
        this.workExperience = 0;
        this.salary = 0.0;
        this.department = "Unknown";
        this.subordinateCount = 0;
    }

    public FactoryManager(String fullName, String birthDate, int workExperience, String department) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.workExperience = workExperience;
        this.department = department;
    }

    public FactoryManager(String fullName, String gender, String birthDate, int workExperience, double salary, String department, int subordinateCount) {
        this.fullName = fullName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.workExperience = workExperience;
        this.salary = salary;
        this.department = department;
        this.subordinateCount = subordinateCount;
    }

    @Override
    public void print() {
        System.out.println("Начальник завода: " + fullName + ", Пол: " + gender +", Дата рождения: " + birthDate+ ", Стаж работы: "+ workExperience + ", Зарплата: "+ salary +", Название отдела: " + department + ", Количество подчинённых: " + subordinateCount);
    }

    @Override
    public double calculateTaxes() {
        return salary * 0.15;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, gender, birthDate, workExperience, salary, department, subordinateCount);
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FactoryManager that = (FactoryManager) o;
        return workExperience == that.workExperience &&
                Double.compare(that.salary, salary) == 0 &&
                subordinateCount == that.subordinateCount &&
                fullName.equals(that.fullName) &&
                gender.equals(that.gender) &&
                birthDate.equals(that.birthDate) &&
                department.equals(that.department);
    }

    @Override
    public String toString() {
        return "Начальник завода {" +
                "ФИО ='" + fullName + '\'' +
                ", Пол ='" + gender + '\'' +
                ", Дата рождения ='" + birthDate + '\'' +
                ", Стаж работы =" + workExperience +
                ", Зарплата =" + salary +
                ", Название отдела ='" + department + '\'' +
                ", Количество подчинённых =" + subordinateCount +
                '}';
    }
}
