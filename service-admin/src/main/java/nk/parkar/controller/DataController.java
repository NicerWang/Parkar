package nk.parkar.controller;

import nk.parkar.pojo.DataItem;
import nk.parkar.service.DataService;
import nk.parkar.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;

@RestController
public class DataController {
    DataService dataService;

    @Autowired
    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    // Defining behavioral data
    // login 0 - userID
    // register 1 - userID
    // getOrders 2 - userID
    // cancel order 3 - userID - Car Number - Price
    // updateInfo 4 - userID
    // reserve 5 - start - end - Car Number - spaceID
    @RequestMapping("/data/{type}/{userID}")
    boolean insertData(@PathVariable("type") Integer type, @PathVariable("userID") String userID, String sa1, String sa2, String sa3, String sa4){
        return dataService.insert(new DataItem(type,userID,sa1,sa2,sa3,sa4));
    }

    @RequestMapping("/get")
    @ResponseBody
    ArrayList<DataItem> getData(HttpServletRequest httpServletRequest){
        if(!JWTUtil.checkAdmin(httpServletRequest.getHeader("token"))) return null;
        Long days = new Date().getTime() / 86400000;
        days++;// Remove this to get data of yesterday.
        Long startDay = days - 1;
        Date start = new Date(startDay * 86400000 - 8 * 3600000);
        Date end = new Date(days * 86400000 - 8 * 3600000);
        return dataService.getAllByTimeInterval(start,end);
    }

}
