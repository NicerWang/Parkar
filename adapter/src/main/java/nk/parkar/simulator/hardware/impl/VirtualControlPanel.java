package nk.parkar.simulator.hardware.impl;

import lombok.SneakyThrows;
import nk.parkar.simulator.hardware.ControlPanel;
import nk.parkar.simulator.utils.HttpUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class VirtualControlPanel implements ControlPanel {

    @Override
    @RequestMapping("/device/panel/license/{license}")
    public String inputLicense(@PathVariable("license") String license) {
        return VirtualLicenseCamera.checkLicense(license);
    }

    @Override
    @SneakyThrows
    @PostMapping("/device/panel/reserve")
    public String inputInfo(String tel,Long end,String credential, String license) {
        return new HttpUtil().makeOrder(tel,credential,new Date(end),license);
    }

}
