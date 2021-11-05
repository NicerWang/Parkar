package nk.parkar.user.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihangyu
 * @create 2021-11-05 10:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class UserLoginVo {

    private String phone;

    private String password;
}
