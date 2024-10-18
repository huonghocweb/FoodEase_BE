package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Request.OrderReturnRequest;
import poly.foodease.Service.OrderReturnService;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/orderReturn")
public class OrderReturnApi {
    @Autowired
    private OrderReturnService orderReturnService;
    @GetMapping
    public ResponseEntity<Object> getAllOrderReturn(
            @RequestPart("pageCurrent") Integer pageCurrent,
            @RequestPart("pageSize") Integer pageSize,
            @RequestPart("sortOrder") String sortOrder,
            @RequestPart("sortBy") String sortBy
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get All OrderReturn");
            result.put("data",orderReturnService.getAllOrderReturn(pageCurrent,pageSize,sortOrder,sortBy));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byOrderId/{orderId}")
    public ResponseEntity<Object> getOrderReturnByOrderId(@PathVariable("orderId") Integer orderId){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get Order Return By OrderId");
            result.put("data",orderReturnService.getOrderReturnByOrderId(orderId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Object> createOrderReturn(
            @RequestPart("orderReturnRequest") OrderReturnRequest orderReturnRequest
            ){
        Map<String,Object> result = new HashMap<>();
        System.out.println("Create OrderReturn : " + orderReturnRequest);
        try {
            result.put("success",true);
            result.put("message","Get All OrderReturn");
            result.put("data",orderReturnService.createOrderReturn(orderReturnRequest));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }


    // Truyền vào mã hóa đơn và boolean xác nhận của admin
    // Sau đó cập nhật lại status của đơn hàng và sau này xử lý hoàn tiền
    @PutMapping("/isApprove/{orderId}")
    public ResponseEntity<Object> approveOrderReturn(
            @PathVariable("orderId") Integer orderId,
            @RequestPart("isApprove") Boolean isApprove
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Approve OrderReturn");
            result.put("data",orderReturnService.approveOrderReturnRequest(orderId, isApprove));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }
}
