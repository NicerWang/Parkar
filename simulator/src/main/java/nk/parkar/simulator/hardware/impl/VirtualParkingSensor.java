package nk.parkar.simulator.hardware.impl;

import nk.parkar.simulator.hardware.ParkingSensor;
import org.springframework.stereotype.Component;

@Component
public class VirtualParkingSensor implements ParkingSensor {
    @Override
    public void occupy(int id) {

    }

    @Override
    public void release(int id) {

    }
}
