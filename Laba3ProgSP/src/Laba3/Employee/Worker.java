package Laba3.Employee;
import java.util.*;

public abstract class Worker implements Object, Employee {
	 protected String fullName;
	    protected String gender;
	    protected String birthDate;
	    protected int workExperience;
	    protected double salary;

	    public Worker(String fullName, String gender, String birthDate, int workExperience, double salary) {
	        this.fullName = fullName;
	        this.gender = gender;
	        this.birthDate = birthDate;
	        this.workExperience = workExperience;
	        this.salary = salary;
	    }
	   
	    @Override
	    public int hashCode() {
	        return Objects.hash(fullName, gender, birthDate, workExperience, salary);
	    }
	    
	    @Override
	    public boolean equals(java.lang.Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        Worker worker = (Worker) o;
	        return workExperience == worker.workExperience &&
	                Double.compare(worker.salary, salary) == 0 &&
	                fullName.equals(worker.fullName) &&
	                gender.equals(worker.gender) &&
	                birthDate.equals(worker.birthDate);
	    }
	    @Override
	    public String toString() {
	        return "Worker{" +
	                "fullName='" + fullName + '\'' +
	                ", gender='" + gender + '\'' +
	                ", birthDate='" + birthDate + '\'' +
	                ", workExperience=" + workExperience +
	                ", salary=" + salary +
	                '}';
	    }
	    public abstract void print();
	    public abstract double calculateTaxes();
}
