package nk.parkar.controller;

import nk.parkar.service.MapReduceService;
import nk.parkar.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mr")
public class MRController {
    private MapReduceService mapReduceService;

    @Autowired
    public void setMapReduceService(MapReduceService mapReduceService) {
        this.mapReduceService = mapReduceService;
    }

    @GetMapping("/{type}")
    public Object getMRResult(@PathVariable("type") Integer type, @RequestHeader("token") String token){
        if(!JWTUtil.checkAdmin(token)) return null;
        switch (type){
            case 0:
                return mapReduceService.getActivityList();
            case 1:
                return mapReduceService.getReservationList();
            case 2:
                return mapReduceService.getCancellationList();
            case 3:
                return mapReduceService.getAvailList();
        }
        return null;
    }
}
