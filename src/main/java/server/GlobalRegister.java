package server;

import lombok.Getter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Getter
public class GlobalRegister {

    private final ServerSocket registerSocket; // серверсокет
    private Socket handlerSocket; //сокет для общения c клиентом
    private BufferedReader reader; // поток чтения из сокета
    private BufferedWriter writer; // поток записи в сокет
    private final List<Handler> handlerList;
    private final int maxSize;

    private final Queue<Task> taskList;

    private final Queue<Task> resultList;

    public void addTask(String task) {
        taskList.add(new Task(task));
    }

    public GlobalRegister(int countOfHandlers) throws IOException {
        taskList = new LinkedList<>();
        resultList = new LinkedList<>();

        registerSocket = new ServerSocket(4003, 1); // серверсокет прослушивает порт 4003
        reader = new BufferedReader(new InputStreamReader(handlerSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(handlerSocket.getOutputStream()));

        handlerList = new ArrayList<>();
        maxSize = countOfHandlers;
        for (int i = 0; i < countOfHandlers; i++) {
            int id = i;

            Handler handler = new Handler() {
                @Override
                public void run() {
                    while (true) {
                        Task task = taskList.remove();
                        if (task != null) {
                            if (task.upCharacterCase()) {
                                System.out.println("Обработчик #" + id + ": " + task.getCommand());
                                taskList.add(task);
                                //getNext().run();
                            } else {
                                resultList.add(task);
                            }
                        }
                    }
                }
            };

            if (!registrationOne(handler)) {
                throw new RuntimeException("Ошибка заполнения глобального регистра!");
            }
        }

        for (Handler handler : handlerList) {
            new Thread(handler).start();
        }
    }

    public String getInfo() {
        return "Количество зарегистрированных обработчиков = " + handlerList.size();
    }

    private boolean registrationOne(Handler handler) {
        if (handlerList.size() < maxSize) {
            if (handlerList.size() > 0) {
                handlerList.get(handlerList.size() - 1).setNext(handler);
                handler.setNext(handlerList.get(0));
            }
            handlerList.add(handler);
            return true;
        }
        return false;
    }

/*    public void registration(List<Handler> handlers) {
        for (Handler handler : handlers)
            if(!registrationOne(handler)){
                throw new RuntimeException("Ошибка заполнения глобального регистра!");
            }
    }*/
}
