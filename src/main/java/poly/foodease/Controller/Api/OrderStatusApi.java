package poly.foodease.Controller.Api;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Service.OrderStatusService;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/orderStatus")
public class OrderStatusApi {
    @Autowired
    OrderStatusService orderStatusService;

    @GetMapping
    public ResponseEntity<Object> getAllOrderStatus() {
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","get All OrderStatus");
            result.put("data",orderStatusService.getAllOrderStatus());
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("{orderStatusId}")
    public ResponseEntity<Object> getOrderStatusById(
            @PathVariable("orderStatusId") Integer orderStatusId
    ) {
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","get OrderStatus By Id");
            result.put("data",orderStatusService.getOrderStatusByOrderStatusId(orderStatusId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

}
