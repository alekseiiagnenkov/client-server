package server;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void upCharacterCase() {
        if (!isReady()) {
            letters.set(count, (char) ((int) letters.get(count) - 32));
            count++;
        }
    }

    public boolean isReady() {
        return count == letters.size();
    }

    @Override
    public String toString() {
        return "Task{" +
                "letters=" + Arrays.toString(letters.toArray()) +
                ", count=" + count +
                '}';
    }
}