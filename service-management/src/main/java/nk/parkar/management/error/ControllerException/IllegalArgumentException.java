package nk.parkar.management.error.ControllerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IllegalArgumentException extends RuntimeException{
    private final List<String> argumentInfoList;
    private final String requestPath;
    private final List<String> description;


    public IllegalArgumentException(String requestPath) {
        argumentInfoList = new ArrayList<>();
        this.requestPath=requestPath;
        this.description = new ArrayList<>();
    }

    public void addArgumentInfo(String...args){
        Collections.addAll(argumentInfoList, args);
    }

    public void addDescription(String newContent){
        this.description.add(newContent);
    }

    public List<String> getArgumentInfoList() {
        return argumentInfoList;
    }

    public List<String> getDescription() {
        return description;
    }

    public String getRequestPath() {
        return requestPath;
    }
}
