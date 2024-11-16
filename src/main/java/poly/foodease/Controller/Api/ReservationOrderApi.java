package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Repository.ReservationOrderRepo;
import poly.foodease.Service.ReservationOrderService;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/reservationOrder")
public class ReservationOrderApi {

    @Autowired
    private ReservationOrderService reservationOrderService ;

    @GetMapping("/getByReservationId/{reservationId}")
    public ResponseEntity<Object> getReservationOrderById(
            @PathVariable("reservationId") Integer reservationId
    )
    {
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get ReservationOrder By ReservationId");
            result.put("data",reservationOrderService.getReservationOrderByReservationId(reservationId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }


    @GetMapping("/changeTable/{reservationOrderId}/{resTableId}")
    public ResponseEntity<Object> changeTableByResTableId(
            @PathVariable("reservationOrderId") Integer reservationOrderId,
            @PathVariable("resTableId") Integer resTableId
    )
    {
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Change Table By ResTableId");
            result.put("data",reservationOrderService.changeTableInReservationOrder(reservationOrderId,resTableId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }
}
