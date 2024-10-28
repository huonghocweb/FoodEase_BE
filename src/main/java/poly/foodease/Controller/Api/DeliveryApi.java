package poly.foodease.Controller.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import poly.foodease.Model.Response.DeliveryData;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/delivery")
public class DeliveryApi {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/estimate")
    public String estimateDeliveryTime(@RequestBody DeliveryData deliveryData) {
        String url = "http://localhost:3000/predict";
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("distance", deliveryData.getDistance());
        requestData.put("weather", deliveryData.getWeather());
        requestData.put("order_time", deliveryData.getOrderTime());
        requestData.put("order_volume", deliveryData.getOrderVolume());

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestData, String.class);
        return response.getBody();
    }
}

