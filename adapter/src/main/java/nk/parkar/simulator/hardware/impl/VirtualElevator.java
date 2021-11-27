package nk.parkar.simulator.hardware.impl;

import nk.parkar.simulator.hardware.Elevator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VirtualElevator implements Elevator {
    static int targetFloor;

    @RequestMapping("/device/elevator")
    int getTargetFloor(){
        return VirtualElevator.targetFloor;
    }
}
