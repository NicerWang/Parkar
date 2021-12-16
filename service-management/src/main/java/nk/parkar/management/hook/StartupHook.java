package nk.parkar.management.hook;

import nk.parkar.management.model.ParkingPrice;
import nk.parkar.management.service.ParkingPriceService;
import nk.parkar.management.util.PriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.stereotype.*;

import java.util.List;

@Component
public class StartupHook implements CommandLineRunner {
    private ParkingPriceService parkingPriceService;

    @Autowired
    public void setParkingPriceService(ParkingPriceService parkingPriceService) {
        this.parkingPriceService = parkingPriceService;
    }

    public void run(String... args) {
        List<ParkingPrice> detail = parkingPriceService.getPriceDetail();
        for(ParkingPrice price : detail){
            if(price.getKey().equals("temporary")) PriceUtil.priceForDayPerHalfHour = price.getValue();
            if(price.getKey().equals("month")) PriceUtil.priceForMonthPerMonth = price.getValue();
            if(price.getKey().equals("year")) PriceUtil.priceForYearPerYear = price.getValue();
        }
    }

}
