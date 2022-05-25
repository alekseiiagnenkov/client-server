package server;

import lombok.Getter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

@Getter
public class Server {
    private Socket clientSocket; //сокет для общения c клиентом
    private BufferedReader reader; // поток чтения из сокета
    private BufferedWriter writer; // поток записи в сокет
    private final ServerSocket serverSocket; // серверсокет
    private final GlobalRegister globalRegister;

    public static Server create(int countOfHandlers) throws IOException {
        return new Server(4004, 1, countOfHandlers);
    }

    private Server(int port, int backlog, int countOfHandlers) throws IOException {
        serverSocket = new ServerSocket(port, backlog); // серверсокет прослушивает порт 4004
        globalRegister = new GlobalRegister(countOfHandlers);
    }

    public void work() {

        List<Handler> list = globalRegister.getHandlerList();
        for (Handler handler : list) {
            new Thread(handler).start();
        }

        while (true) {

            globalRegister.refresh();

            try {
                clientSocket = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                long time = System.currentTimeMillis();

                int countOfRequest = 0;
                String request = getMessageByClient();
                while (!"".equals(request)) {
                    countOfRequest++;
                    //System.out.println("[" + Thread.currentThread().getName() + "] Command: " + request);
                    if (isCorrectCommand(request)) {
                        globalRegister.addTask(request);
                    } else {
                        sendMessageToClient("Uncorrected command!");
                    }
                    request = getMessageByClient();
                }

                System.out.println("Time1:"+(System.currentTimeMillis() - time));

                while (countOfRequest != 0) {
                    String response = globalRegister.getResult();
                    if (response != null) {
                        sendMessageToClient(response);
                        countOfRequest--;
                    }
                }

                System.out.println("Time2:"+(System.currentTimeMillis() - time));

                clientClose();

            } catch (Exception ignored) {} finally {
                System.out.println("Communication with the client is completed!\n");
            }
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
            if (command.charAt(i) < 97 || command.charAt(i) > 122) return false;
        }
        return true;
    }
}