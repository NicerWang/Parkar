package nk.parkar.simulator.hardware;

import org.springframework.stereotype.Component;


public interface LicenseCamera {
    boolean scanLicense(String license);
}
