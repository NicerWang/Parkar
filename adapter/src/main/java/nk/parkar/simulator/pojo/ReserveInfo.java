package nk.parkar.simulator.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveInfo {
    Boolean hasOrder;
    ParkingSpace space;
    ParkingOrder currentOrder;
}
