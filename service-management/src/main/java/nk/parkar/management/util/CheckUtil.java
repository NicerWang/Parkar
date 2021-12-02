package nk.parkar.management.util;

import nk.parkar.management.error.ControllerException.TransactionException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CheckUtil {
    public static boolean checkUserId(String userId) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://81.70.254.128/api/user/userExit/" + userId);
        CloseableHttpResponse response = null;
        TransactionException transactionException = new TransactionException("[Transaction] Check userId");
        try {
            response = httpClient.execute(httpGet);
            if (response == null) {
                transactionException.addDescription("[Failed] Check userId by empty response");
                throw transactionException;
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
                return (Boolean) jsonObject.get("success");
            } else {
                transactionException.addDescription("[Failed] Check userId by statusCode:" + statusCode);
                throw transactionException;
            }
        } catch (IOException | JSONException e) {
            transactionException.addDescription("[Failed] Check userId by execute request error");
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
    public static String checkTime(long start,long end){
        if(start > end)
            return "Start time should not be later than End time.";
        if(start < 1000000000000L || start > 9999999999999L)
            return "Only Accept 13 number timestamp.";
        if(end < 1000000000000L || end > 9999999999999L)
            return "Only Accept 13 number timestamp.";
        return null;
    }
}
