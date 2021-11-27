package nk.parkar.simulator.hardware.impl;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import nk.parkar.simulator.hardware.LicenseCamera;
import nk.parkar.simulator.pojo.ReserveInfo;
import nk.parkar.simulator.utils.HttpUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VirtualLicenseCamera implements LicenseCamera {

    private static ObjectMapper objectMapper = new ObjectMapper();


    @Override
    @RequestMapping("/device/{license}")
    public String scanLicense(@PathVariable("license") String license) {
        return VirtualLicenseCamera.checkLicense(license);
    }

    @SneakyThrows
    public static String checkLicense(String license){
        String info = new HttpUtil().sendLicenseInfo(license);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        ReserveInfo reserveInfo = objectMapper.readValue(info, ReserveInfo.class);
        if(reserveInfo.getHasOrder())
            VirtualElevator.targetFloor = reserveInfo.getSpace().getFloor();
        return info;
    }


}
