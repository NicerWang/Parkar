package nk.parkar.management.schedule;

import nk.parkar.management.service.ParkingSpaceService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;

@Component
@EnableScheduling
public class StatisticTask {
    @Autowired
    public void setParkingSpaceService(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    ParkingSpaceService parkingSpaceService;

    @Scheduled(cron = "0 10 0 * * ?", zone = "Asia/Shanghai")
    public void UpdateOccupyRate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH) - 1,0,0,0);
        long start = calendar.getTime().getTime() / 1000 * 1000;
        double aver;
        int total = parkingSpaceService.getAllSpaces().size() * 48;
        int avail = 0;
        for(int i = 0; i < 48; i++){
            avail += parkingSpaceService.querySpaceByTime(start, start + 30L * 60 * 1000 - 1000).size();
            start = start + 30L * 60 * 1000;
        }
        aver = 1 - (double) avail / total;
        aver *= 100;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://81.70.254.128/api/admin/data/4/?sa1=" + aver );
        httpPost.setHeader("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkNTliZWE4YTcxMWY0ZTFhNDhhMmE5NGYyZjVkNTRjNSIsImlzcyI6IlBhcmthciIsImFkbWluIjpmYWxzZSwiZXhwIjoxNjQyMDc5NjA3fQ.j2xQ3j7Truem9ssLyi95IheUPCpG8o_I50qCKgPe_VA");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if (response == null) {
                System.err.println("Empty response when update occupy rate");
                return;
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                System.err.println("Error when update occupy rate");
            }
        } catch (IOException ioException) {
            System.err.println("ioException when update occupy rate");
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}