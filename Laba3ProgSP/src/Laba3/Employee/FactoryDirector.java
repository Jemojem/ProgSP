package Laba3.Employee;
import java.util.*;

public class FactoryDirector implements Object, Employee {

	 private String fullName;
	    private String gender;
	    private String birthDate;
	    private int workExperience;
	    private double salary;
	    private String electionTerm;
	    
	    public FactoryDirector() {
	        this.fullName = "Unknown";
	        this.gender = "Unknown";
	        this.birthDate = "Unknown";
	        this.workExperience = 0;
	        this.salary = 0.0;
	        this.electionTerm = "Unknown";
	    }


	    public FactoryDirector(String fullName, String birthDate, int workExperience, String electionTerm) {
	        this.fullName = fullName;
	        this.birthDate = birthDate;
	        this.workExperience = workExperience;
	        this.electionTerm = electionTerm;
	    }

	    public FactoryDirector(String fullName, String gender, String birthDate, int workExperience, double salary, String electionTerm) {
	        this.fullName = fullName;
	        this.gender = gender;
	        this.birthDate = birthDate;
	        this.workExperience = workExperience;
	        this.salary = salary;
	        this.electionTerm = electionTerm;
	    }

	    @Override
	    public void print() {
	    	System.out.println("Директор завода: " + fullName + ", Пол: " + gender +", Дата рождения: " + birthDate+ ", Стаж работы: "+ workExperience + ", Зарплата: "+ salary + ", Дата найма: " + electionTerm);
	    }

	    @Override
	    public double calculateTaxes() {
	        return salary * 0.18;
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(fullName, gender, birthDate, workExperience, salary, electionTerm);
	    }

	    @Override
	    public boolean equals(java.lang.Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        FactoryDirector director = (FactoryDirector) o;
	        return workExperience == director.workExperience &&
	                Double.compare(director.salary, salary) == 0 &&
	                electionTerm == director.electionTerm &&
	                fullName.equals(director.fullName) &&
	                gender.equals(director.gender) &&
	                birthDate.equals(director.birthDate);
	    }

	    @Override
	    public String toString() {
	        return "Директор завода {" +
	                "ФИО ='" + fullName + '\'' +
	                ", Пол ='" + gender + '\'' +
	                ", Дата рождения ='" + birthDate + '\'' +
	                ", Стаж работы =" + workExperience +
	                ", Зарплата =" + salary +
	                ", Срок избрания =" + electionTerm +
	                '}';
	    }
}
