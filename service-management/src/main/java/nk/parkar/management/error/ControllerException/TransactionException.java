package nk.parkar.management.error.ControllerException;

import java.util.ArrayList;
import java.util.List;

public class TransactionException extends RuntimeException{
    private String processName;
    private List<String> description;

    public TransactionException(String processName) {
        this.processName = processName;
        this.description = new ArrayList<>();
    }

    public void addDescription(String newContent){
        description.add(newContent);
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }
}
