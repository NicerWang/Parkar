package nk.parkar.simulator.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpace {
    Integer spaceId;

    Boolean occupied;

    Integer mode;

    Boolean ban;

    Boolean booked;

    Integer floor;

    Integer xCoordinate;

    Integer yCoordinate;

    Integer test;
}
