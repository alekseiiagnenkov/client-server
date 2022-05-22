package server;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Handler implements Runnable {
    private Handler next = null;
}