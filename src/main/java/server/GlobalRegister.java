package server;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class GlobalRegister {

    private static final ConcurrentLinkedQueue<Task> taskList = new ConcurrentLinkedQueue<>();

    private static final ConcurrentLinkedQueue<Task> resultList = new ConcurrentLinkedQueue<>();

    private final List<Handler> handlerList;

    private final int countOfHandlers;

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
                            Task task = getTask();
                            if (task != null) {
                                if (!task.isReady()) {
                                    task.upCharacterCase();
                                    System.out.println("[" + Thread.currentThread().getName() + "] Handler #" + id + ": " + task.getCommand());
                                    sentToNextHandler(task);
                                } else {
                                    resultList.add(task);
                                }
                            }
                        } catch (Exception ignored) {}
                    }
                }

                private Task getTask() {
                    Task task = null;
                    if (currentTask != null) {
                        task = currentTask;
                        currentTask = null;
                    } else if (!taskList.isEmpty() && !newTask)
                        task = taskList.remove();
                    return task;
                }

                private void sentToNextHandler(Task task) {
                    Handler next = getNext();
                    next.newTask = true;
                    while (next.currentTask != null) {}
                    next.currentTask = task;
                    next.newTask = false;
                }
            };

            registrationOne(handler);
        }
    }

    public String getInfo() {
        return "Number of registered handlers = " + handlerList.size();
    }

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

        for (Handler handler : handlerList) {
            handler.newTask = false;
            handler.currentTask = null;
        }
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

    @Override
    public String toString() {
        return "{\nhandlerList=" + Arrays.toString(handlerList.toArray()) +
                "\ncountOfHandlers=" + countOfHandlers +
                "\ntakeList= " + taskList.size() +
                "\nresultList= " + resultList.size() +
                "\n}";
    }
}
