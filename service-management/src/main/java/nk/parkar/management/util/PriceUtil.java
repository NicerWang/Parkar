package nk.parkar.management.util;

import nk.parkar.management.mapper.ParkingPriceMapper;
import nk.parkar.management.service.ParkingPriceService;
import org.springframework.beans.factory.annotation.Autowired;

public class PriceUtil {
    public static double priceForDayPerHalfHour;
    public static double priceForMonthPerMonth;
    public static double priceForYearPerYear;

    public static double getPrice(int mode, Long startTime, Long endTime){
        switch (mode) {
            case 0:
                long dis = endTime - startTime;
                return priceForDayPerHalfHour * (dis / 1800000 + 1.0);
            case 1:
                return priceForMonthPerMonth;
            case 2:
                return priceForYearPerYear;
        }
        return -1;

    }
}
