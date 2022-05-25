package server;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Handler implements Runnable {

/*    protected Handler next = null;

    protected Task currentTask = null;

    protected boolean newTask = false;*/

/*    @Override
    public String toString() {
        return
                "{ currentTask=" + currentTask +
                ", newTask=" + newTask +
                '}';
    }*/
}
