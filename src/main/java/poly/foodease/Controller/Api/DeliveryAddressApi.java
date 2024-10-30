package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Request.DeliveryAddressRequest;
import poly.foodease.Service.DeliveryAddressService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/deliveryAddress")
public class DeliveryAddressApi {

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    @GetMapping
    public ResponseEntity<Object> getAllDeliveryAddress(
            @RequestParam("pageCurrent") Integer pageCurrent,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sortOrder") String sortOrder,
            @RequestParam("sortBy") String sortBy
    ){
        Map<String,Object> result = new HashMap<>();
        Sort sort = Sort.by(new Sort.Order(Objects.equals("asc", sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Pageable pageable= PageRequest.of(pageCurrent, pageSize, sort);
        try {
            result.put("success",true);
            result.put("message","Get All DeliveryAddress");
            result.put("data",deliveryAddressService.getAllDeliveryAddress(pageable));
        }catch (Exception e ){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getById/{deliveryAddressId}")
    public ResponseEntity<Object> getDeliveryAddressById(
            @PathVariable("deliveryAddressId") Integer deliveryAddressId
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get DeliveryAddress By Id");
            result.put("data",deliveryAddressService.getDeliveryAddressById(deliveryAddressId));
        }catch (Exception e ){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }


    @GetMapping("/getByUserName/{userName}")
    public ResponseEntity<Object> getDeliveryAddressByUserName(
            @PathVariable("userName") String userName,
            @RequestParam("pageCurrent") Integer pageCurrent,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sortOrder") String sortOrder,
            @RequestParam("sortBy") String sortBy
    ){
        Map<String,Object> result = new HashMap<>();
        Sort sort = Sort.by(new Sort.Order(Objects.equals("asc", sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Pageable pageable= PageRequest.of(pageCurrent, pageSize, sort);
            System.out.println("Get By userName" + userName);
            System.out.println(deliveryAddressService.getDeliveryAddressByUserName(userName,pageable).getContent());
        try {
            result.put("success",true);
            result.put("message","Get DeliveryAddress By UserName");
            result.put("data",deliveryAddressService.getDeliveryAddressByUserName(userName,pageable));
        }catch (Exception e ){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Object> createDeliveryAddress(
            @RequestPart("deliveryAddressRequest")DeliveryAddressRequest deliveryAddressRequest
            ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Create DeliveryAddress");
            result.put("data",deliveryAddressService.createDeliveryAddress(deliveryAddressRequest));
        }catch (Exception e ){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }


    @PutMapping("/{deliveryAddressId}")
    public ResponseEntity<Object> updateDeliveryAddress(
            @PathVariable("deliveryAddressId") Integer deliveryAddressId,
            @RequestPart("deliveryAddressRequest")DeliveryAddressRequest deliveryAddressRequest
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Update DeliveryAddress");
            result.put("data",deliveryAddressService.updateDeliveryAddress(deliveryAddressId, deliveryAddressRequest));
        }catch (Exception e ){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{deliveryAddressId}")
    public ResponseEntity<Object> updateDeliveryAddress(
            @PathVariable("deliveryAddressId") Integer deliveryAddressId
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Delete  DeliveryAddress By Id");
            result.put("data",deliveryAddressService.removerDeliveryAddress(deliveryAddressId));
        }catch (Exception e ){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }
}
