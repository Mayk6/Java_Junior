package Lesson4.Homework;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


import java.sql.*;
import java.util.List;

public class Homework {

    private static final String URL = "jdbc:mysql://localhost:3307";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public static void task1() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement statement = connection.createStatement();
        statement.execute("DROP SCHEMA Homework");
        statement.execute("CREATE SCHEMA Homework");
        statement.execute("create table Homework.book (id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(45), author VARCHAR(45))");
        statement.execute("create table Homework.author (id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(45))");
        statement.execute("INSERT INTO Homework.book (name, author)" +
                "VALUES" +
                "('Книга1', 'Автор1')," +
                "('Книга2', 'Автор2')," +
                "('Книга3', 'Автор3')," +
                "('Книга4', 'Автор4')," +
                "('Книга5', 'Автор5')," +
                "('Книга6', 'Автор6')," +
                "('Книга7', 'Автор7')," +
                "('Книга8', 'Автор8')," +
                "('Книга9', 'Автор9')," +
                "('Книга10', 'Автор10');");

        statement.execute("SELECT * from Homework.book WHERE author='Автор5'");
        ResultSet set = statement.executeQuery("SELECT * from Homework.book WHERE author='Автор5'");
        while (set.next()) {
            System.out.println(set.getString(2) + " " + set.getString(3) + " " + set.getInt(1));
        }
        statement.close();
        connection.close();
    }

    public static void task2() {
        Session session = openSession();
        session.beginTransaction();

        Author author = new Author("Автор1");
        session.save(author);
        Author author1 = new Author("Avtor1");
        session.save(author1);

        Book book = new Book("Kniga1", author);
        session.save(book);
        book = new Book("Kniga2", author);
        session.save(book);
        book = new Book("Kniga3", author);
        session.save(book);
        book = new Book("Kniga4", author);
        session.save(book);
        book = new Book("Kniga5", author1);
        session.save(book);
        book = new Book("Kniga6", author1);
        session.save(book);
        book = new Book("Kniga7", author1);
        session.save(book);
        book = new Book("Kniga8", author);
        session.save(book);
        book = new Book("Kniga9", author1);
        session.save(book);
        book = new Book("Kniga10", author);
        session.save(book);

        session.getTransaction().commit();
        List<Book> books = session.createQuery("from Book where author=" + author1.getId(), Book.class).getResultList();
        books.forEach(b -> System.out.println(b.toString()));
        session.close();

    }

    private static void task3() {


        Session session = openSession();
        session.beginTransaction();
        Author author = new Author("Автор1");
        session.save(author);
        Author author2 = new Author("Автор2");
        session.save(author2);
        Book book = new Book("Kniga1", author);
        session.save(book);
        book = new Book("Harry Potter", author2);
        session.save(book);
        session.getTransaction().commit();

        List<Book> books = session.createQuery("from Book", Book.class).getResultList();
        books.forEach(b -> System.out.println(b.toString()));

        session.close();

    }

    public static Session openSession() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        return sessionFactory.openSession();
    }

    public static void main(String[] args) throws SQLException {
        task3();
    }


}
