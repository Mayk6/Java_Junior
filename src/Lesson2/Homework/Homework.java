package Lesson2.Homework;

public class Homework {

    /**
     * Написать свою систему запуска тестов
     *
     * Идея - написать нечто умеющее запускать тесты
     */



    public static void main(String[] args) {
        TestProcessor.runTest(MyTest.class);

    }

    static class MyTest {

        @Test(order = -2)
        void firstTest() {
            System.out.println("firstTest Run");
        }

        @Test()
        void secondTest() {
            System.out.println("secondTest Run");
        }

        @Test(order = 5)
        void thirdTest() {
            System.out.println("Third test Run");
        }
        @BeforeEach
        void beforeTest() {
            System.out.println("Before test");
        }

        @AfterEach
        void afterTest(){
            System.out.println("After test");
        }
        @Skip
        @AfterEach
        void afterTest2(){
            System.out.println("After test 2");
        }
    }
}
