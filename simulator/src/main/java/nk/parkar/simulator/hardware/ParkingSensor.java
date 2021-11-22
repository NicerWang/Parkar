package nk.parkar.simulator.hardware;

import org.springframework.stereotype.Component;


public interface ParkingSensor {
    void occupy(int id);
    void release(int id);
}
