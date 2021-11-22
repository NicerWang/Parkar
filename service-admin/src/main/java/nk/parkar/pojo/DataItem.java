package nk.parkar.pojo;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class DataItem {
    Date time;
    Integer type;
    String userId;
    String sa1;
    String sa2;
    String sa3;
    String sa4;

    public DataItem(Integer type, String userId, String sa1, String sa2, String sa3, String sa4) {
        this.type = type;
        this.userId = userId;
        this.sa1 = sa1;
        this.sa2 = sa2;
        this.sa3 = sa3;
        this.sa4 = sa4;
    }
}
