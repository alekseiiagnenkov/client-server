package server;

import lombok.Getter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Getter
public class Server {
    private Socket clientSocket; //сокет для общения c клиентом
    private BufferedReader reader; // поток чтения из сокета
    private BufferedWriter writer; // поток записи в сокет
    private final ServerSocket serverSocket; // серверсокет
    private final GlobalRegister globalRegister;
    private final static int countOfHandlers = 3;

    public static Server create() throws IOException {
        return new Server(4004, 1);
    }
    private Server(int port, int backlog) throws IOException {
        serverSocket = new ServerSocket(port, backlog); // серверсокет прослушивает порт 4004
        globalRegister = new GlobalRegister(countOfHandlers);
    }

    public void work() throws IOException {
        clientSocket = serverSocket.accept();
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        try {
            String request = getMessageByClient();
            while (!"".equals(request)) {
                System.out.println("Получена команда: " + request);
                if (isCorrectCommand(request)) {
                    globalRegister.addTask(request);
                    //globalRegister.start();
                    sendMessageToClient(globalRegister.getResultList().remove().getCommand());
                } else {
                    sendMessageToClient("Некорректная команда!");
                }
                request = getMessageByClient();
            }
        } finally {
            clientClose();
            System.out.println("Общение с клиентом завершено!");
        }
    }

    public void close() throws IOException {
        this.serverSocket.close();
    }

    private void sendMessageToClient(String response) throws IOException {
        writer.write(response);
        writer.newLine();
        writer.flush();
    }

    private String getMessageByClient() throws IOException {
        return this.reader.readLine();
    }

    private void clientClose() throws IOException {
        reader.close();
        writer.close();
        clientSocket.close();
    }

    private static boolean isCorrectCommand(String command) {
        for (int i = 0; i < command.length(); i++) {
            if (command.charAt(i) < 97 || command.charAt(i) > 122)
                return false;
        }
        return true;
    }
}
