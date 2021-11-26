package nk.parkar.simulator.hardware.impl;

import lombok.SneakyThrows;
import nk.parkar.simulator.hardware.ControlPanel;
import nk.parkar.simulator.utils.HttpUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class VirtualControlPanel implements ControlPanel {
    private String license;
    @Override
    public String inputLicense(String license) {
        this.license = license;
        return VirtualLicenseCamera.checkLicense(license);
    }

    @Override
    @SneakyThrows
    public String inputInfo(String tel, Long end, String credential) {
        return new HttpUtil().makeOrder(tel,credential,new Date(end),this.license);
    }

}
