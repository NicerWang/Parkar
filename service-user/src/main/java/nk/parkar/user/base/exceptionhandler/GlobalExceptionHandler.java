package nk.parkar.user.base.exceptionhandler;

import nk.parkar.user.base.exceptionhandler.exception.TokenNotExitException;
import nk.parkar.user.base.exceptionhandler.exception.UserNotExitException;
import nk.parkar.user.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lihangyu
 * @create 2021-10-30 11:19
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message(e.getMessage());
    }

    @ExceptionHandler(TokenNotExitException.class)
    @ResponseBody
    public R error(TokenNotExitException e){
        e.printStackTrace();
        return R.error().message("Token not exit!");
    }

    @ExceptionHandler(UserNotExitException.class)
    @ResponseBody
    public R error(UserNotExitException e){
        e.printStackTrace();
        return R.error().message("User not exit!");
    }
}
