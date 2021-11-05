package nk.parkar.user.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author lihangyu
 * @create 2021-11-05 10:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class UserInfoVo {
    private String id;

    private String username;

    private String sex;

    private String phone;

    private String address;

    private Integer balance;

    private Integer isDisabled;

    private Date registerTime;

    private Date lastLoginTime;
}
