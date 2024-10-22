package poly.foodease.Controller.Api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Service.ReservationService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
@CrossOrigin("*")

public class ReservationApi {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<Object> getAllReservation(
            @RequestParam("pageCurrent") Integer pageCurrent,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sortOrder") String sortOrder,
            @RequestParam("sortBy") String sortBy
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get All Reservation");
            result.put("data",reservationService.getAllReservation(pageCurrent, pageSize, sortOrder, sortBy));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getByUserName/{userName}")
    public ResponseEntity<Object> getReservationByUserName(
            @PathVariable("userName") String userName
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get Reservation By UserName");
            result.put("data",reservationService.getReservationByUserName(userName));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<Object> getReservationById(
            @PathVariable("reservationId") Integer reservationId
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get Reservation By Reservation Id");
            result.put("data",reservationService.getReservationByReservationId(reservationId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Object> createReservation(
            @RequestPart("reservationRequest") ReservationRequest reservationRequest
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Create Reservation");
            result.put("data",reservationService.createReservation(reservationRequest));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<Object> updateReservation(
            @PathVariable("reservationId") Integer reservationId,
            @RequestPart("reservationRequest") ReservationRequest reservationRequest
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Update Reservation ");
            result.put("data",reservationService.updateReservation(reservationId, reservationRequest));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getByTableIdAndDate/{tableId}")
    public ResponseEntity<Object> getReservationByTableIdAndDate(
            @PathVariable("tableId") Integer tableId,
            @RequestParam("dateCheckTime")LocalDate dateCheckTime
            ){
        Map<String,Object> result = new HashMap<>();
        System.out.println("getByTableIdAndDate");
        System.out.println(" DateTime Cehck " +dateCheckTime);
        System.out.println(reservationService.getReservedByTableIdAndDate(tableId , dateCheckTime));
        try {
            result.put("success",true);
            result.put("message","Update Reservation ");
            result.put("data",reservationService.getReservedByTableIdAndDate(tableId, dateCheckTime));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }
}
