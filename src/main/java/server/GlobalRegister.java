package server;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class GlobalRegister {

    private static final ConcurrentLinkedQueue<Task> taskList = new ConcurrentLinkedQueue<>();
    //private static final ConcurrentLinkedQueue<Task> processingList = new ConcurrentLinkedQueue<>();
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
        //processingList.clear();
        resultList.clear();
    }

    public GlobalRegister(int countOfHandlers) {
        this.countOfHandlers = countOfHandlers;
        handlerList = new ArrayList<>();
        for (int i = 0; i < this.countOfHandlers; i++) {
            int id = i + 1;

            Handler handler = new Handler() {

/*                private Handler next;
                private Task currentTask;

                public Handler getNext() {
                    return next;
                }

                public Task getCurrentTask() {
                    return currentTask;
                }

                public void setNext(Handler next) {
                    this.next = next;
                }

                public void setCurrentTask(Task currentTask) {
                    this.currentTask = currentTask;
                }*/

                @Override
                public void run() {
                    while (true) {
                        try {
                            Task task = null;
/*                            if (!processingList.isEmpty())
                                task = processingList.remove();*/
                            if (getCurrentTask() != null)
                                task = getCurrentTask();

                            else if (!taskList.isEmpty())
                                task = taskList.remove();

                            if (task != null) {
                                if (!task.isBlock()) {
                                    task.setBlock(true);
                                    if (!task.isReady()) {
                                        Task newTask = task.upCharacterCase();
                                        System.out.println("[" + Thread.currentThread().getName() + "] Handler #" + id + ": " + newTask.getCommand());

                                        task.setBlock(false);
                                        getNext().setCurrentTask(newTask);
                                        setCurrentTask(null);
                                        //processingList.add(newTask);
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
            //handlerList.add(handler);

        }
    }

    public String getInfo() {
        return "Number of registered handlers = " + handlerList.size();
    }

    private void registrationOne(Handler handler) {
        if (handlerList.size() < countOfHandlers) {
            if (handlerList.size() > 0) {
                handlerList.get(handlerList.size() - 1).setNext(handler);
                handler.setNext(handlerList.get(0));
            }
            handlerList.add(handler);
        }
    }

    public static Queue<Task> getResultList() {
        return resultList;
    }
}
