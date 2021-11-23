package nk.parkar.simulator.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpUtil {
    static String requestBase = "http://81.70.254.128/api/";

    public String genericRequest(String url){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(requestBase + url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode != 200) return null;
        String content = null;
        try {
            content = EntityUtils.toString(response.getEntity(), "UTF-8");
            httpClient.close();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public String sendLicenseInfo(String license){
        return genericRequest("management/machine/elevator/space/" + license);
    }

    public String updateParkingSensor(boolean isOccupy){
        if(isOccupy){
            return genericRequest("management/machine/elevator");
        }
        else return genericRequest("management/machine/elevator");
    }


}
