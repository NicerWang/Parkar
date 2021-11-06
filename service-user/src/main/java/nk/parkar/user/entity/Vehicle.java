package nk.parkar.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author lihnagyu
 * @since 2021-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "vehicle_id", type = IdType.ASSIGN_UUID)
    private String vehicleId;

    private String userId;


}
