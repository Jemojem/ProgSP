package Laba3.Employee;
import java.util.*;

public class FactoryManager implements Object, Employee {

	protected String fullName;
    protected String gender;
    protected String birthDate;
    protected int workExperience;
    protected double salary;
    protected String departmentName;
    protected int numberOfEmployees;

    public FactoryManager(String fullName, String gender, String birthDate, int workExperience, double salary, String departmentName, int numberOfEmployees) {
        this.fullName = fullName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.workExperience = workExperience;
        this.salary = salary;
        this.departmentName = departmentName;
        this.numberOfEmployees = numberOfEmployees;
    }

    @Override
    public void print() {
        System.out.println("Factory Manager: " + fullName + ", Department: " + department);
    }

    @Override
    public double calculateTaxes() {
        return salary * 0.15;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, birthDate, salary, department);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FactoryManager that = (FactoryManager) o;
        return Double.compare(that.salary, salary) == 0 &&
                fullName.equals(that.fullName) &&
                birthDate.equals(that.birthDate) &&
                department.equals(that.department);
    }

    @Override
    public String toString() {
        return "FactoryManager{" +
                "fullName='" + fullName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", salary=" + salary +
                ", department='" + department + '\'' +
                '}';
    }
}
