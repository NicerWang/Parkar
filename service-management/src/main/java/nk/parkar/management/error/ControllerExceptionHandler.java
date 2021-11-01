package nk.parkar.management.error;

import nk.parkar.management.error.ControllerException.IllegalArgumentException;
import nk.parkar.management.error.ControllerException.TransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String,Object> handleIllegalArgumentException(IllegalArgumentException exception){
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("requestPath",exception.getRequestPath());
        retMap.put("message",exception.getDescription());
        retMap.put("illegalArguments",exception.getArgumentInfoList());
        return retMap;
    }

    @ExceptionHandler(TransactionException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,Object> handleTransactionException(TransactionException exception){
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("transactionName",exception.getProcessName());
        retMap.put("message",exception.getDescription());
        return retMap;
    }

}
