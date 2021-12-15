package nk.parkar.schedule;

import nk.parkar.service.MapReduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class StatisticTask {
    MapReduceService mapReduceService;

    @Autowired
    public void setMapReduceService(MapReduceService mapReduceService) {
        this.mapReduceService = mapReduceService;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void updateMRTables(){
        mapReduceService.mapReduce();
    }
}
