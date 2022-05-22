package client;

import java.io.*;
import java.net.Socket;

public class ClientSession {

    private final Socket socket; //????? ??? ???????

    private final BufferedReader reader; // ????? ?????? ?? ?????

    private final BufferedWriter writer; // ????? ?????? ? ?????

    static ClientSession createSession() throws IOException {
        return new ClientSession("localhost", 4004);
    }

    private ClientSession(String host, int port) throws IOException {
        socket = new Socket(host, port); // ?????? ?? ?????????? ? ????????
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void closeSession() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }

    public String getMessage() throws IOException {
        return reader.readLine();
    }

    public void sendMessage(String word) throws IOException {
        writer.write(word);
        writer.newLine();
        writer.flush();
    }
}
