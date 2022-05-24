package server;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class GlobalRegister {

    private static final ConcurrentLinkedQueue<Task> taskList = new ConcurrentLinkedQueue<>();

    private static final ConcurrentLinkedQueue<Task> resultList = new ConcurrentLinkedQueue<>();

    private final List<Handler> handlerList;
    private final int countOfHandlers;

    public void addTask(String task) {
        taskList.add(new Task(task));
    }

    public String getResult() {
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.remove().getCommand();
    }

    public void refresh() {
        taskList.clear();
        resultList.clear();
    }

    public GlobalRegister(int countOfHandlers) {
        this.countOfHandlers = countOfHandlers;
        handlerList = new ArrayList<>();
        for (int i = 0; i < this.countOfHandlers; i++) {
            int id = i + 1;

            Handler handler = new Handler() {

                @Override
                public void run() {
                    while (true) {
                        try {
                            Task task = null;
                            if (currentTask != null) {
                                task = currentTask;
                                currentTask = null;
                            }

                            else if (!taskList.isEmpty() && !newTask)
                                task = taskList.remove();

                            if (task != null) {
                                if (!task.isBlock()) {
                                    task.setBlock(true);
                                    if (!task.isReady()) {
/*                                        Task task1 = task.upCharacterCase();
                                        System.out.println("[" + Thread.currentThread().getName() + "] Handler #" + id + ": " + task1.getCommand());

                                        Handler next = getNext();
                                        next.newTask = true;
                                        while (next.currentTask != null) {}
                                        next.currentTask = task1;*/
                                        task.upCharacterCase();
                                        System.out.println("[" + Thread.currentThread().getName() + "] Handler #" + id + ": " + task.getCommand());

                                        Handler next = getNext();
                                        next.newTask = true;
                                        while (next.currentTask != null) {}
                                        next.currentTask = task;
                                        next.newTask = false;
                                    } else {
                                        resultList.add(task);
                                    }
                                    task.setBlock(false);
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            };

            registrationOne(handler);
        }
    }

    public String getInfo() {
        return "Number of registered handlers = " + handlerList.size();
    }

    private void registrationOne(Handler handler) {
        if (handlerList.size() < countOfHandlers) {
            if (!handlerList.isEmpty()) {
                handlerList.get(handlerList.size() - 1).setNext(handler);
                handler.setNext(handlerList.get(0));
            }
            handlerList.add(handler);
        }
    }
}
