package Lesson1.Homework;

import java.util.Random;

public class Employee {

    private static Random random = new Random();
    private static int counter = 0;
    final String name;
    final int age;
    double salary;
    String department;

    public Employee() {
        counter++;
        this.name = "Сотрудник" + counter;
        this.age = random.nextInt(18,50);
        this.salary = random.nextDouble(5_000, 200_000);
        this.department = "Department: " + random.nextInt(1,5);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return String.format("Name: %s/ Age: %d/ Salary: %f/ Department: %s", name, age, salary, department);
    }
}
