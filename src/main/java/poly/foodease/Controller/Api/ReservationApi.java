package poly.foodease.Controller.Api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Service.ReservationService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/reservation")
@CrossOrigin("*")

public class ReservationApi {

    @Autowired
    private ReservationService reservationService;


    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping
    public ResponseEntity<Object> getAllReservation(
            @RequestParam("pageCurrent") Integer pageCurrent,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sortOrder") String sortOrder,
            @RequestParam("sortBy") String sortBy,
            @RequestParam(value = "startDate",required = false) LocalDate startDate,
            @RequestParam(value = "endDate" , required = false) LocalDate endDate,
            @RequestParam(value = "keyWord", required = false) String keyWord
    ){
        Map<String,Object> result = new HashMap<>();
        Sort sort = Sort.by(new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Pageable pageable = PageRequest.of(pageCurrent, pageSize, sort);
        try {
            result.put("success",true);
            result.put("message","Get All Reservation");
            if(startDate != null && endDate != null){
                System.out.println("Filter Date" + startDate + endDate);
                System.out.println(reservationService.getReservationByBookDate(startDate, endDate, pageable).getContent());
                result.put("data",reservationService.getReservationByBookDate(startDate, endDate, pageable));
            }else if(!keyWord.isEmpty() && startDate == null && endDate == null){
                System.out.println("Find By Key Word" + keyWord);
                System.out.println(" startDate " +startDate + endDate);
                System.out.println(reservationService.getReservationByKeyWord(keyWord, pageable).getContent());
                result.put("data", reservationService.getReservationByKeyWord(keyWord, pageable));
            }
            else{
                result.put("data",reservationService.getAllReservation( pageable));
            }
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getByUserName/{userName}")
    public ResponseEntity<Object> getReservationByUserName(
            @PathVariable("userName") String userName,
            @RequestParam("pageCurrent") Integer pageCurrent,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sortOrder") String sortOrder,
            @RequestParam("sortBy") String sortBy

    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get Reservation By UserName");
            result.put("data",reservationService.getReservationByUserName(userName,pageCurrent,pageSize,sortOrder,sortBy));
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

    @PreAuthorize("hasRole('ADMIN')")
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


    @PreAuthorize("hasRole('ADMIN')")
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


    // Hàm kiếm tra lây ra các lịch đặt bàn trước theo ngày đưa vào
    @GetMapping("/getByTableIdAndDate/{tableId}")
    public ResponseEntity<Object> getReservationByTableIdAndDate(
            @PathVariable("tableId") Integer tableId,
            @RequestParam("dateCheckTime")LocalDate dateCheckTime
            ){
        Map<String,Object> result = new HashMap<>();
        System.out.println("Check History Reser by Date ");
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

    @GetMapping("/cancelReservationRequest/{reservationId}")
    public ResponseEntity<Object> changeReservationStatus(
            @PathVariable("reservationId") Integer tableId
    ){
        Map<String,Object> result = new HashMap<>();
        System.out.println("cancelReservationRequest");
        try {
            result.put("success",true);
            result.put("message","Cancel Request Reservation ");
            result.put("data",reservationService.cancelRequestReservation(tableId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checkinReservation/{reservationId}/{checkinCode}")
    public ResponseEntity<Object> checkinReservation(
            @PathVariable("reservationId") Integer reservationId,
            @PathVariable("checkinCode") String checkinCode
    ){
        Map<String,Object> result = new HashMap<>();
        System.out.println("checkinReservation");
        try {
            result.put("success",true);
            result.put("message","Checkin  Reservation ");
            result.put("data",reservationService.checkinReservation(reservationId, checkinCode));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

//    @PutMapping("/orderFoodToReservation/{reservationId}")
//    public ResponseEntity<Object> orderFoodToReservation(
//            @PathVariable("reservationId") Integer reservationId,
//            @RequestPart("foodIds") List<Integer > foodIds
//            ){
//        Map<String,Object> result = new HashMap<>();
//        System.out.println("checkinReservation");
//        try {
//            result.put("success",true);
//            result.put("message","Update Food To Reservation ");
//            result.put("data",reservationService.orderFootToReservation(reservationId, foodIds));
//        }catch (Exception e){
//            result.put("success",false);
//            result.put("message",e.getMessage());
//            result.put("data",null);
//        }
//        return ResponseEntity.ok(result);
//    }
}
