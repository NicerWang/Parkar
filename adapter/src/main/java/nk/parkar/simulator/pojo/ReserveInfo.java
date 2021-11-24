package nk.parkar.simulator.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveInfo {
    Boolean hasOrder;
    Integer spaceId;
    Date startTime;
    Date endTime;
    int floor;
    String userId;
}
