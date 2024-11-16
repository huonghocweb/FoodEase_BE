package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Entity.Reservation;
import poly.foodease.Model.Request.ReservationOrderPaymentRequest;
import poly.foodease.Model.Response.ReservationOrderPaymentResponse;
import poly.foodease.Service.ReservationOrderPaymentService;
import poly.foodease.Service.ReservationService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/reservationOrderPaymentApi")
public class ReservationOrderPaymentApi {

    @Autowired
    private ReservationOrderPaymentService reservationOrderPaymentService;
    @Autowired
    private ReservationService reservationService;


    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllReservationOrderPayment(
            @RequestParam("pageCurrent") Integer pageCurrent,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sortOrder") String sortOrder,
            @RequestParam("sortBy") String sortBy
    ){
        Map<String,Object> result = new HashMap<>();
        Sort sort = Sort.by(new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Pageable pageable = PageRequest.of(pageCurrent,  pageSize, sort);
        try {
            result.put("success",true);
            result.put("message","Get All  Reservation OrderPayment");
            result.put("data",reservationOrderPaymentService.getAllReservationOrderPayment(pageable));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }
    @GetMapping("/getById/{reservationOrderPaymentId}")
    public ResponseEntity<Object> getReservationOrderPaymentById(
            @PathVariable("reservationOrderPaymentId") Integer reservationOrderPaymentId
    ){
        Map<String,Object> result = new HashMap<>();
        System.out.println("reservationOrderPaymentId " + reservationOrderPaymentId );
        try {
            result.put("success",true);
            result.put("message","Get ReservationOrderPayment By Id");
            result.put("data",reservationOrderPaymentService.getById(reservationOrderPaymentId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }


    @PostMapping("/{reservationOrderId}/{paymentMethod}/{totalAmount}")
    public ResponseEntity<Object> getReservationById(
            @PathVariable("reservationOrderId") Integer reservationOrderId,
            @PathVariable("paymentMethod") Integer paymentMethod,
            @PathVariable("totalAmount") Double totalAmount
    ){
        Map<String,Object> result = new HashMap<>();
        System.out.println("reservationOrderId " + reservationOrderId + "pay method " + paymentMethod);
        try {
            result.put("success",true);
            result.put("message","Get Reservation By Reservation Id");

            ReservationOrderPaymentResponse reservationOrderPayment = reservationOrderPaymentService.createReservationOrderPayment(reservationOrderId, paymentMethod ,totalAmount);
            if (reservationOrderPayment != null){
                reservationService.checkoutReservation(reservationOrderPayment.getReservationOrder().getReservation().getReservationId());
            }else{
                System.out.println("Payment Failed");
            }
            result.put("data",reservationOrderPayment);
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getByReservationId/{reservationId}")
    public ResponseEntity<Object> getReservationById(
            @PathVariable("reservationId") Integer reservationId
    ){
        Map<String,Object> result = new HashMap<>();
        System.out.println("reservationOrderId " + reservationId );
        try {
            result.put("success",true);
            result.put("message","Get ReservationById");
            result.put("data",reservationOrderPaymentService.getReservationOrderPaymentByReservationId(reservationId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }


}
