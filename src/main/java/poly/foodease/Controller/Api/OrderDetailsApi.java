package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Service.OrderDetailsService;

import java.util.HashMap;
import java.util.Map;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/orderDetails")
public class OrderDetailsApi {
    @Autowired
    OrderDetailsService orderDetailsService;

    @GetMapping("/orderDetailsHistory/{orderId}")
    public ResponseEntity<Object> getOrderDetailsByOrderId(
            @PathVariable("orderId") Integer orderId
    ){
        Map<String,Object> result = new HashMap<>();
        System.out.println("Get OrderDetails By OrderId");
        try {
            result.put("success", true);
            result.put("message","Get OrderDetails by orderId");
            result.put("data",orderDetailsService.getOrderDetailsByOrderId(orderId));
        }catch (Exception e){
            result.put("success", false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }
}
