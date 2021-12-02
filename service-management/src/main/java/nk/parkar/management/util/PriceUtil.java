package nk.parkar.management.util;

public class PriceUtil {
    public static double priceForDayPerHalfHour = 2.8;
    public static double priceForMonthPerMonth = 188.0;
    public static double priceForYearPerYear = 888.0;

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
