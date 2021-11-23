package nk.parkar.simulator.hardware;

import org.springframework.stereotype.Component;


public interface ParkingSensor {
    void occupy(Integer id);
    void release(Integer id);
}
