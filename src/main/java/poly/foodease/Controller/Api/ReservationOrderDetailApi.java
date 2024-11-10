package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Service.ReservationOrderDetailService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/reservationOrderDetail")
public class ReservationOrderDetailApi {
    @Autowired
    private ReservationOrderDetailService reservationOrderDetailService;

    @PostMapping("/createReservationOrderDetails/{reservationId}")
    public ResponseEntity<Object> createReservationOrderDetail(
            @PathVariable("reservationId") Integer reservationId,
            @RequestPart("foodOrderItem") Map<Integer,Integer> foodOrderItem
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Create Reservation OrderDetail");
            result.put("data",reservationOrderDetailService.createReservationOrderDetail(reservationId,foodOrderItem ));
        }catch (Exception e){
            result.put("success",true);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);

    }
}
