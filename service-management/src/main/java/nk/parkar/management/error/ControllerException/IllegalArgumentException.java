package nk.parkar.management.error.ControllerException;

import java.util.ArrayList;
import java.util.List;

public class IllegalArgumentException extends RuntimeException {
    private final String requestPath;
    private final List<String> description;


    public IllegalArgumentException(String requestPath) {
        this.requestPath = requestPath;
        this.description = new ArrayList<>();
    }


    public void addDescription(String newContent) {
        this.description.add(newContent);
    }

    public List<String> getDescription() {
        return description;
    }

    public String getRequestPath() {
        return requestPath;
    }
}
