package nk.parkar.pojo;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MRCancellation {
    Date time;
    int value;

    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public MRCancellation(Date time, int value) {
        this.time = time;
        this.value = value;
    }
}
