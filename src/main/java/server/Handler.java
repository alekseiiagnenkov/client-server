package server;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Handler implements Runnable {
    protected Handler next = null;

    protected Task currentTask = null;

    protected boolean newTask = false;
    public void setCurrentTask(Task currentTask) {
        if (this.currentTask == null || currentTask == null)
            this.currentTask = currentTask;
        else
            next.setCurrentTask(currentTask);
    }
}
