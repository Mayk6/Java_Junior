package Lesson3.Homework;

import Lesson3.Seminar.SerializablePerson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class Homework {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SerializablePerson serializablePerson = new SerializablePerson("Gogi");
        serialize(serializablePerson);
        deSerialize("Lesson3.Seminar.SerializablePerson_d88ee3e6-6f1b-4907-8d35-d0b4e903b94a");
    }

    public static void serialize(Object object) throws IOException {
        Path path = Path.of(object.getClass().getName() + "_" + UUID.randomUUID().toString());
        if (!(object instanceof Serializable)) {
            System.out.println("Объект класса " + object.getClass().getName() + " не сериализуем(не имплементирует интерфейс Serializable");
        } else {
            OutputStream outputStream = Files.newOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
        }
    }

    public static void deSerialize(String str) throws IOException, ClassNotFoundException {
        Path path = Path.of(str);
        try {
            if (Files.exists(path)) {
                InputStream inputStream = Files.newInputStream(path);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                System.out.println(objectInputStream.readObject());
                objectInputStream.close();
                Files.delete(path);
            } else {
                System.out.println("Файла нет");
            }
        } catch (StreamCorruptedException e) {
            System.out.println("Данные в файле некорректны");
        }
    }
}
