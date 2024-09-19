package Laba3.Employee;
import java.util.*;

abstract class Worker implements Object, Employee {
    protected String fullName;
    protected String gender;
    protected String birthDate;
    protected int workExperience;
    protected double salary;
    protected int workshopNumber;
    protected int rank;

    public Worker() {
        this.fullName = "Unknown";
        this.gender = "Unknown";
        this.birthDate = "Unknown";
        this.workExperience = 0;
        this.salary = 0.0;
        this.workshopNumber = 0;
        this.rank = 0;
    }

    public Worker(String fullName, String gender, String birthDate, int workExperience) {
        this.fullName = fullName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.workExperience = workExperience;
        this.salary = 0.0;
        this.workshopNumber = 0;
        this.rank = 0;
    }

    public Worker(String fullName, String gender, String birthDate, int workExperience, double salary, int workshopNumber, int rank) {
        this.fullName = fullName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.workExperience = workExperience;
        this.salary = salary;
        this.workshopNumber = workshopNumber;
        this.rank = rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, gender, birthDate, workExperience, salary, workshopNumber, rank);
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return workExperience == worker.workExperience &&
                Double.compare(worker.salary, salary) == 0 &&
                workshopNumber == worker.workshopNumber &&
                rank == worker.rank &&
                fullName.equals(worker.fullName) &&
                gender.equals(worker.gender) &&
                birthDate.equals(worker.birthDate);
    }

    @Override
    public String toString() {
        return "Рабочий {" +
                "ФИО ='" + fullName + '\'' +
                ", Пол ='" + gender + '\'' +
                ", Дата рождения ='" + birthDate + '\'' +
                ", Стаж работы =" + workExperience +
                ", Зарплата =" + salary +
                ", Номер цеха =" + workshopNumber +
                ", Разряд =" + rank +
                '}';
    }

    public abstract void print();
    public abstract double calculateTaxes();
}