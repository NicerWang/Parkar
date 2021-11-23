package nk.parkar.management.util;

import nk.parkar.management.error.ControllerException.IllegalArgumentException;
import nk.parkar.management.error.ControllerException.TransactionException;
import nk.parkar.management.service.ParkingSpaceService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.regex.Pattern;

public class CheckUtil {
    private static ParkingSpaceService parkingSpaceService;

    @Autowired
    public void setParkingSpaceService(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    public static boolean checkUserId(String userId){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://39.106.179.185:8080/userExit/"+userId);
        httpGet.setHeader("token", "checkUserId");
        CloseableHttpResponse response = null;
        TransactionException transactionException = new TransactionException("checking userId");
        try {
            response = httpClient.execute(httpGet);
            if(response==null){
                transactionException.addDescription("response is null");
                throw transactionException;
            }
            int statusCode=response.getStatusLine().getStatusCode();
            if (statusCode==200) {
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
                return (Boolean)jsonObject.get("success");
            }
            else{
                transactionException.addDescription("checking userId statusCode:"+statusCode);
                throw transactionException;
            }
        } catch (IOException | JSONException e) {
            transactionException.addDescription("complex internal service error caused by stupid code in checkUserId");
            throw transactionException;
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

    public static boolean checkSpaceIdFormat(IllegalArgumentException illegalArgumentException, String spaceIdStr){
        String pattern = "^\\d+$";
        if(!Pattern.matches(pattern,spaceIdStr)){
            illegalArgumentException.addDescription("illegal spaceId:"+spaceIdStr);
            illegalArgumentException.addArgumentInfo("spaceId");
            return false;
        }
        return true;
    }

    public static boolean checkNumberFormat(String numberStr){
        String pattern = "^\\d+$";
        if(!Pattern.matches(pattern,numberStr)){
            return false;
        }
        return true;
    }

    public static boolean checkOccupied(IllegalArgumentException illegalArgumentException,String occupiedStr){
        if(occupiedStr.equals("0")||occupiedStr.equals("1")){
            return true;
        }
        else{
            illegalArgumentException.addDescription("illegal occupied: "+occupiedStr);
            illegalArgumentException.addArgumentInfo("occupied");
            return false;
        }
    }

    public static boolean checkMode(IllegalArgumentException illegalArgumentException, String mode){
        if(!mode.equals("day")&&!mode.equals("month")&&!mode.equals("year")){
            illegalArgumentException.addDescription("illegal mode:"+mode);
            illegalArgumentException.addArgumentInfo("mode");
            return false;
        }
        else{
            return true;
        }
    }

    public static boolean checkPaidFormat(String paidStr){
        return paidStr.equals("0") || paidStr.equals("1");
    }

    public static boolean checkTimeFormat(IllegalArgumentException illegalArgumentException, String startTimeStr, String endTimeStr){
        String pattern = "^\\d{10}|\\d{13}$";
        if(!Pattern.matches(pattern,startTimeStr)){
            illegalArgumentException.addDescription("illegal startTime:"+startTimeStr+" ==> startTime is not a unix_timestamp");
            illegalArgumentException.addArgumentInfo("startTime");
            return false;
        }
        if(!Pattern.matches(pattern,endTimeStr)){
            illegalArgumentException.addDescription("illegal endTime:"+endTimeStr+"  ==> endTime is not a unix_timestamp");
            illegalArgumentException.addArgumentInfo("endTime");
            return false;
        }
        return true;
    }

    public static boolean checkSpaceIdValue(Integer spaceId){
        return (parkingSpaceService.querySpaceById(spaceId)==null);
    }
}
