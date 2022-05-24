package server;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Task {
    private List<Character> letters;
    private int count;
    private boolean block;

    public Task(String command) {
        count = 0;
        letters = new ArrayList<>();
        for (int i = 0; i < command.length(); i++) {
            letters.add(command.charAt(i));
        }
    }

    public String getCommand() {
        StringBuilder sb = new StringBuilder();
        for (Character character : letters) {
            sb.append(character);
        }
        return sb.toString();
    }

    public Task upCharacterCase() {
        if (isReady())
            return this;
        else {
            Task task = new Task(this.getCommand());
            task.count = count;
            task.letters.set(task.count, (char) ((int) task.letters.get(task.count) - 32));
            task.count++;
            return task;
        }
    }

    public boolean isReady() {
        return count == letters.size();
    }
}