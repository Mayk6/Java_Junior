package Lesson1.Homework;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {


        //Создать список из 1_000 рандомных чисел от 1 до 1_000_000
        Random random = new Random();
        List<Integer> list = Stream.generate(() -> random.nextInt(0, 1_000_000))
                .limit(1000)
                .toList();

        //Найти максимальное
        System.out.println(list.stream().max(Comparator.naturalOrder()));

        // Все числа, большие, чем 500_000, умножить на 5, отнять от них 150 и просуммировать
        System.out.println(list.stream()
                .filter(it -> it > 500_000).map(it -> (it * 5 - 150)).mapToLong(it -> it).sum());

        // Найти количество чисел, квадрат которых меньше, чем 100_000
        System.out.println(list.stream().filter(it -> (it * it) < 100_000).count());


        //Создать список из 10-20 сотрудников
        ArrayList<Employee> employees = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            employees.add(new Employee());
        }

        // Вывести список всех различных отделов (department) по списку сотрудников
        employees.stream()
                .map(Employee::getDepartment)
                .distinct()
                .forEach(System.out::println);

        // Всем сотрудникам, чья зарплата меньше 10_000, повысить зарплату на 20%
        employees.stream()
                .filter(it -> it.getSalary() < 10_000.0)
                .forEach(it -> it.setSalary(it.getSalary() * 1.2));
        employees.forEach(System.out::println);

        // Из списка сотрудников с помощью стрима создать Map<String, List<Employee>> с отделами и сотрудниками внутри отдела
        Map<String, List<Employee>> map1 = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
        map1.entrySet().forEach(System.out::println);

        // Из списка сотрудников с помощью стрима создать Map<String, Double> с отделами и средней зарплатой внутри отдела
        Map<String, Double> map2 = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.averagingDouble(Employee::getSalary)));
        map2.entrySet().forEach(System.out::println);

    }


}
