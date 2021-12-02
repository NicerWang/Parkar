package nk.parkar.simulator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HttpUtil {
    static String requestBase = "http://81.70.254.128/api/";

    public String genericRequest(String url, String type){
        return genericRequest(url,type,"","");
    }

    @SneakyThrows
    public String genericRequest(String url, String type, String token, String info){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpRequestBase request = null;
        switch (type) {
            case "get":
                request = new HttpGet(requestBase + url);
                break;
            case "post":
                request = new HttpPost(requestBase + url);
                break;
            case "put":
                request = new HttpPut(requestBase + url);
                break;
        }
        if(!info.isEmpty() && request != null){
            request.addHeader("Content-Type", "application/json");
            ((HttpPost)request).setEntity(new StringEntity(info));
        }

        if(!token.isEmpty() && request != null)
            request.setHeader("token",token);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
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
        return genericRequest("management/machine/camera/" + license,"get");
    }

    public String updateParkingSensor(boolean isOccupy, Integer id){
        if(isOccupy){
            return genericRequest("management/machine/sensor/" + id + "?occupied=1", "put");
        }
        else return genericRequest("management/machine/sensor/" + id + "?occupied=0", "put");
    }

    public String makeOrder(String tel, String pwd, Date end, String license) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Long now = (new Date()).getTime();
        if(now > end.getTime()) return null;
        String response = genericRequest("user/login","post","","{\n" +
                "  \"password\": \"" + pwd + "\",\n" +
                "  \"phone\": \"" + tel + "\"\n" +
                "}");
        HashMap<String,Object> map = objectMapper.readValue(response, new TypeReference<HashMap<String, Object>>() {});
        String token = (String) ((HashMap<String,Object>)map.get("data")).get("token");
        if(token == null || token.isEmpty()) return null;
        response = genericRequest("management/order/space/?startTime=" + now + "&endTime=" + end.getTime(),"get", token,"");
        map = objectMapper.readValue(response, new TypeReference<HashMap<String, Object>>() {});
        List<Integer> avails = (List<Integer>) map.get("availableSpaceIdList");
        if(avails.isEmpty()) return "No FREE Space!";
        else {
            String select = avails.get(0).toString();
            genericRequest("management/machine/order?spaceId=" + select + "&endTime=" + end.getTime() + "&licenseNumber=" + license, "post", token,"");
        }
        return sendLicenseInfo(license);

    }


}
