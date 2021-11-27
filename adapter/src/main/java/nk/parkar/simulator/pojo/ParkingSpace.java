package nk.parkar.simulator.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpace {
    Integer spaceId;

    Byte occupied;

    Integer mode;

    Byte ban;

    Byte booked;

    Integer floor;

    Integer xCoordinate;

    Integer yCoordinate;

    Integer test;
}
