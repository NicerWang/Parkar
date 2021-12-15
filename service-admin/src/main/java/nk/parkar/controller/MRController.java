package nk.parkar.controller;

import nk.parkar.service.MapReduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mr")
public class MRController {
    private MapReduceService mapReduceService;

    @Autowired
    public void setMapReduceService(MapReduceService mapReduceService) {
        this.mapReduceService = mapReduceService;
    }

    @GetMapping("/{type}")
    public Object getMRResult(@PathVariable("type") Integer type){
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
