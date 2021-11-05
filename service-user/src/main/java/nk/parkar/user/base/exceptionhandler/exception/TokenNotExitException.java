package nk.parkar.user.base.exceptionhandler.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lihangyu
 * @create 2021-11-05 9:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class TokenNotExitException extends Exception{
}
