package Lesson2.Homework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TestProcessor {

    /**
     * Данный метод находит все void методы без аргументов в классе и запускает их.
     * Для запуска создается тестовый объект для запуска теста без аргументов
     */
    public static void runTest(Class<?> testClass) {
        final Constructor<?> declaredConstructor;
        try {
            declaredConstructor = testClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Для класса \"" + testClass.getName() + "\" не найден конструктор без аргументов");
        }


        final Object testObj;
        try {
            testObj = declaredConstructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать объект класса \"" + testClass.getName() + "\"");
        }

        List<Method> priority = new ArrayList<>();
        for (Method method : testClass.getDeclaredMethods()) {

            if (method.isAnnotationPresent(Test.class ) && !method.isAnnotationPresent(Skip.class)) {
                checkTestMethod(method);
                priority.add(method);
            }

        }

        priority.sort(new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                if (o1.getAnnotation(Test.class).order() > o2.getAnnotation(Test.class).order()) {
                    return 1;
                } else if (o1.getAnnotation(Test.class).order() < o2.getAnnotation(Test.class).order()) {
                    return -1;
                }
                return 0;
            }
        });

        Deque<Method> res = new ArrayDeque<>(priority);


        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeEach.class) && !method.isAnnotationPresent(Skip.class)) {
                checkTestMethod(method);
                res.addFirst(method);
            } else if (method.isAnnotationPresent(AfterEach.class) && !method.isAnnotationPresent(Skip.class)) {
                checkTestMethod(method);
                res.addLast(method);
            }
        }

        res.forEach(it -> runTest(it, testObj));
    }

    private static void checkTestMethod(Method method) {
        if (!method.getReturnType().isAssignableFrom(void.class) && method.getParameterCount() == 0) {
            throw new IllegalArgumentException("Метод \"" + method.getName() + "\" должен быть void и не иметь аргументов");
        }
    }

    private static void runTest(Method testMethod, Object testObj) {
        try {
            testMethod.invoke(testObj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось запустить тестовый метод \"" + testMethod.getName() + "\"");
        }
    }
}
