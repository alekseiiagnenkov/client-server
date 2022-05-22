package server;

public class Admin {

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        try {
            Server server = Server.create();
            System.out.println("Сервер запущен!\n");
            System.out.println(server.getGlobalRegister().getInfo() + '\n');
            try {
                while (true) {
                    server.work();
                }
            } finally {
                server.close();
                System.out.println("Сервер закрыт!");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
