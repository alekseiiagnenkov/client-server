package server;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Handler implements Runnable {
    private Handler next = null;
    public Task currentTask = null;

    public void setCurrentTask(Task currentTask) {
        if (this.currentTask == null || currentTask == null)
            this.currentTask = currentTask;
        else
            next.setCurrentTask(currentTask);
    }
}
