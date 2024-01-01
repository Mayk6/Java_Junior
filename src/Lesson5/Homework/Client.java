package Lesson5.Homework;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        final Socket client = new Socket("localhost", Server.PORT);

            new Thread(() -> {

                try (Scanner input = new Scanner(client.getInputStream())) {
                    while (true) {
                        if (input.hasNextLine() ) {
                            System.out.println(input.nextLine());
                        }
                         if (client.isClosed()){
                            System.exit(0);
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }).start();

        new Thread(() -> {
            try (PrintWriter output = new PrintWriter(client.getOutputStream(), true)) {
                Scanner consoleScanner = new Scanner(System.in);
                while (true) {
                    String consoleInput = consoleScanner.nextLine();
                    output.println(consoleInput);
                    if (client.isClosed()){
                        System.exit(0);
                    }
                    if (Objects.equals("q", consoleInput)) {
                        client.close();
                        System.exit(1);
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}