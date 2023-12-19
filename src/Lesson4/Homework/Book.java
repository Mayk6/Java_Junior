package Lesson4.Homework;

import javax.persistence.*;

@Entity
@Table
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @OneToOne(targetEntity = Author.class)
    private Author author;

    public Book(String name, Author author) {
        this.name = name;
        this.author = author;
    }

    public Book() {

    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
