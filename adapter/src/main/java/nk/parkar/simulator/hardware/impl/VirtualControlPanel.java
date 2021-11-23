package nk.parkar.simulator.hardware.impl;

import nk.parkar.simulator.hardware.ControlPanel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class VirtualControlPanel implements ControlPanel {

    @Override
    public String inputLicense(String license) {
        return VirtualLicenseCamera.checkLicense(license);
    }

    @Override
    public String inputInfo(String tel, Long end, String QR) {
        return null;
    }

}
