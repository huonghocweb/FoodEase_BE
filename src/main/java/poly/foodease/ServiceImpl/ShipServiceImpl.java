package poly.foodease.ServiceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import poly.foodease.Service.ShipService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ShipServiceImpl implements ShipService {
    @Autowired
    private RestTemplate restTemplate;

    private final String GHN_URL_PUBLIC = "https://online-gateway.ghn.vn/shiip/public-api/";
    private final String TOKEN = "58983ae0-8322-11ef-bbb6-a2edf3918909";
    private final Integer SHOP_ID = 5368838;
    private final String GET_SERVICE = "v2/shipping-order/available-services";
    private final String GET_PROVINCE= "master-data/province";
    private final String GET_DISTRICT = "master-data/district";
    private final String GET_WARD = "master-data/ward";
    private final Integer FROM_DISTRICT_ID = 1442;
    private final String FROM_WARD_CODE ="20109";
    private final String CALCULATE_FEE = "v2/shipping-order/fee";
    private final String GET_LEAD_TIME = "v2/shipping-order/leadtime";


    public String getProvince (){
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String > response= restTemplate.exchange(GHN_URL_PUBLIC + GET_PROVINCE, HttpMethod.POST,requestEntity,String.class);
        return response.getBody();
    }
    public String getDistrict(Integer provinceId) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo một map cho nội dung yêu cầu
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("province_id", provinceId);

        // Chuyển đổi map thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(GHN_URL_PUBLIC + GET_DISTRICT, HttpMethod.POST, requestEntity, String.class);
        return response.getBody();
    }
    public String getWard(Integer districtId) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Tạo một map cho nội dung yêu cầu
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("district_id", districtId);

        // Chuyển đổi map thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(GHN_URL_PUBLIC + GET_WARD, HttpMethod.POST, requestEntity, String.class);
        return response.getBody();
    }

    public String getService(Integer toDistrictId) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String,Object> requestBody = new HashMap<>();
        requestBody.put("shop_id",SHOP_ID);
        requestBody.put("from_district", FROM_DISTRICT_ID);
        requestBody.put("to_district",toDistrictId);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody,headers);
        ResponseEntity<String> response = restTemplate.exchange(GHN_URL_PUBLIC + GET_SERVICE, HttpMethod.POST, requestEntity, String.class);

        return response.getBody();
    }

    public Object calculateFee(Integer serviceId , Integer toDistrictId , String toWardId) throws JsonProcessingException {
        System.out.println(serviceId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ShopId", String.valueOf(SHOP_ID));

        // Tạo request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("service_type_id", serviceId);
        requestBody.put("from_district_id",FROM_DISTRICT_ID );
        requestBody.put("from_ward_code",FROM_WARD_CODE);
        requestBody.put("to_district_id", toDistrictId);
        requestBody.put("to_ward_code", toWardId);
        requestBody.put("height", 10);
        requestBody.put("length", 10);
        requestBody.put("weight", 100);
        requestBody.put("width", 10);
        requestBody.put("insurance_value", 0);
        requestBody.put("coupon", null);

        // Tạo danh sách items
        Map<String, Object> item1 = new HashMap<>();
        item1.put("name", "FOOD1");
        item1.put("quantity", 1);
        item1.put("height", 10);
        item1.put("weight", 100);
        item1.put("length", 10);
        item1.put("width", 10);
        // Thêm item vào danh sách items
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(item1);
        requestBody.put("items", items);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(GHN_URL_PUBLIC + CALCULATE_FEE, HttpMethod.POST, requestEntity, String.class);
        Integer leadTime = this.getLeadTime(serviceId, toDistrictId, toWardId);
        System.out.println(leadTime);

        Map<String,Object> data = new HashMap<>();
        data.put("feeData",response.getBody());
        data.put("leadTimeData",leadTime);
        return data;
    }

    public Integer getLeadTime(Integer serviceId , Integer toDistrictId , String toWardId) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("from_district_id", FROM_DISTRICT_ID);
        requestBody.put("from_ward_code", FROM_WARD_CODE);
        requestBody.put("to_district_id",toDistrictId);
        requestBody.put("to_ward_code",toWardId);
        requestBody.put("service_id",serviceId);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(GHN_URL_PUBLIC + GET_LEAD_TIME, HttpMethod.POST, requestEntity, String.class);
        ObjectMapper responseMapper = new ObjectMapper();
        JsonNode jsonResponse = responseMapper.readTree(response.getBody());
        long leadTime = jsonResponse.path("data").path("leadtime").asLong();
        System.out.println("lead Time " + leadTime);
        Integer leadTimeInHours = (int) (leadTime / 36000000);
        return leadTimeInHours;
    }


}
