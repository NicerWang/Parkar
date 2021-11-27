package nk.parkar.simulator.hardware.impl;

import nk.parkar.simulator.hardware.ParkingSensor;
import nk.parkar.simulator.utils.HttpUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VirtualParkingSensor implements ParkingSensor {

    @RequestMapping("/device/occupy/{id}")
    @Override
    public void occupy(@PathVariable("id") Integer id) {
        new HttpUtil().updateParkingSensor(true,id);
    }

    @Override
    @RequestMapping("/device/release/{id}")
    public void release(@PathVariable("id") Integer id) {
        new HttpUtil().updateParkingSensor(false,id);
    }
}
