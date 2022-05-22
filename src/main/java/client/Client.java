package client;

import java.util.Random;

public class Client {

    public static void main(String[] args) {
        try {
            ClientSession clientSession = ClientSession.createSession();
            try {

                //Scanner scanner = new Scanner(System.in);
                String command;
/*                do {
                    System.out.print("Ожидание команды:");
                    //command = scanner.nextLine();
                    command = generateRandomString()
                    System.out.println("Команда: " + command);
                    clientSession.sendMessage(command);
                    if (command.equals(""))
                        break;
                    System.out.println("Ответ сервера: " + clientSession.getMessage());
                } while (true);*/

                for (int i = 0; i < 10; i++) {
                    command = generateRandomString(i);
                    System.out.println("Команда: " + command);
                    clientSession.sendMessage(command);
                    System.out.println("Ответ сервера: " + clientSession.getMessage());
                }
            } finally {
                clientSession.closeSession();
                System.out.println("Клиент был закрыт.");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static String generateRandomString(int i) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < i + 1; j++) {
            sb.append(Character.toChars(random.nextInt(97, 122)));
        }
        return sb.toString();
    }
}
