package nk.parkar.user.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lihangyu
 * @create 2021-11-05 9:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class UserUpdateVo {
    private String username;

    private String password;

    private String phone;

    private String address;
}
