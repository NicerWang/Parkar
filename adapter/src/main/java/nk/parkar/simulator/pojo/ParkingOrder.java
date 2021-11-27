package nk.parkar.simulator.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingOrder {
    Integer orderId;
    String userId;
    String licenseNumber;
    Integer spaceId;
    Date startTime;
    Date endTime;
    BigDecimal price;
    Integer mode;
    Byte paid;


}