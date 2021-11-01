package nk.parkar.management.error.ControllerException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

public class IllegalArgumentException extends RuntimeException{
    private List<String> argumentInfoList;
    private String requestPath;
    private List<String> description;


    public IllegalArgumentException(String requestPath) {
        argumentInfoList = new ArrayList<>();
        this.requestPath=requestPath;
        this.description = new ArrayList<>();
    }

    public void addArgumentInfo(String...args){
        for(String arg:args){
            argumentInfoList.add(arg);
        }
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
