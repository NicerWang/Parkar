package nk.parkar.controller;

import nk.parkar.pojo.DataItem;
import nk.parkar.service.DataService;
import nk.parkar.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/data/{type}/")
    boolean insertData(@PathVariable("type") Integer type, String sa1, String sa2, String sa3, String sa4, @RequestHeader("token") String token){
        String userID = JWTUtil.check(token);
        if(userID == null) return false;
        return dataService.insert(new DataItem(type,userID,sa1,sa2,sa3,sa4));
    }

    @GetMapping("/get")
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
