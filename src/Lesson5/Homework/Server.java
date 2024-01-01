package Lesson5.Homework;

import lombok.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    public static final int PORT = 8181;
    private static long clientIdCounter = 1L;
    private static Map<Long, SocketWrapper> clients = new HashMap<>();
    private static ArrayList<Long> adminList = new ArrayList<>();
    private static final String adminPass = "221313124FF";

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);
            while (true) {
                final Socket client = server.accept();
                final long clientId = clientIdCounter++;

                SocketWrapper wrapper = new SocketWrapper(clientId, client);
                System.out.println("Подключился новый клиент[" + wrapper + "]");
                clients.put(clientId, wrapper);

                new Thread(() -> {
                    try (Scanner input = wrapper.getInput(); PrintWriter output = wrapper.getOutput()) {
                        output.println("Подключение успешно. Список всех клиентов: " + clients);

                        while (input.hasNextLine()) {
                            String clientInput = input.nextLine();
                            boolean personalMsg = false;
                            boolean admin = false;
                            long destinationId = -1L;

                            if (clientInput.length() > 1) {
                                personalMsg = clientInput.substring(0, 1).equals("@");
                                if (Character.isDigit(clientInput.charAt(1))) {
                                    destinationId = Long.parseLong(clientInput.substring(1, 2));
                                }
                            }

                            if (clientInput.equals(adminPass)) {
                                adminList.add(wrapper.getId());
                                wrapper.getOutput().println("Вам выданы права администратора");
                            }


                            if (adminList.contains(wrapper.getId()) && clientInput.contains("kick")) {
                                long id = Long.parseLong(clientInput.substring(5, 6));
                                clients.get(id).getOutput().println("Вас кикнули с сервера.");
                                clients.get(id).getSocket().close();
                                clients.remove(id);
                                clients.values().forEach(it -> it.getOutput().println("Клиент[" + id + "] кикнут с сервера"));
                            }

                            if (personalMsg && destinationId != -1L) {
                                if (clients.get(destinationId) != null) {
                                    SocketWrapper destination = clients.get(destinationId);
                                    destination.getOutput().println(clientId + ": " + clientInput.substring(2));
                                } else {
                                    wrapper.getOutput().println("Клиента с ID: " + destinationId + " не существует");
                                }
                            } else if (!clientInput.equals(adminPass)) {
                                clients.values().forEach(it -> it.getOutput().println(clientId + ": " + clientInput));
                            }

                            if (Objects.equals("q", clientInput)) {
                                // todo разослать это сообщение всем остальным клиентам
                                clients.remove(clientId);
                                clients.values().forEach(it -> it.getOutput().println("Клиент[" + clientId + "] отключился"));
                                wrapper.getOutput().println("Вы отключены от сервера");
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        }
    }
}

@Getter
class SocketWrapper implements AutoCloseable {

    private final long id;
    private final Socket socket;
    private final Scanner input;
    private final PrintWriter output;

    SocketWrapper(long id, Socket socket) throws IOException {
        this.id = id;
        this.socket = socket;
        this.input = new Scanner(socket.getInputStream());
        this.output = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }

    @Override
    public String toString() {
        return String.format("%s", socket.getInetAddress().toString());
    }
}
