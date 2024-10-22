package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Model.Entity.OrderDetails;
import poly.foodease.Model.Response.OrderDetailsResponse;
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Repository.OrderDetailsRepo;
import poly.foodease.Repository.OrderRepo;
import poly.foodease.Service.OrderDetailsService;
import poly.foodease.Service.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/orderDetails")
public class OrderDetailsApi {
    @Autowired
    OrderDetailsService orderDetailsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    OrderDetailsRepo orderDetailsRepository;

    @Autowired
    OrderRepo orderRepository;

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
