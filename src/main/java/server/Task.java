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

    Task(String command) {
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

    public boolean upCharacterCase() {
        if(count<letters.size()) {
            letters.set(count, (char) ((int) letters.get(count) - 32));
            count++;
            return true;
        } return false;
    }
}
