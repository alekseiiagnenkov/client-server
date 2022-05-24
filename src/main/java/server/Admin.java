package server;

public class Admin {
    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        try {
            Server server = Server.create();
            System.out.println("Server started!\n");
            System.out.println(server.getGlobalRegister().getInfo() + '\n');
            try {
                server.work();
            } finally {
                server.close();
                System.out.println("Server closed!");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}