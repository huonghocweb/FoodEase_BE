package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Request.CouponStorageRequest;
import poly.foodease.Service.CouponStorageService;

import java.util.HashMap;
import java.util.Map;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/couponStorage")
public class CouponStorageApi {

    @Autowired
    CouponStorageService couponStorageService;

    @GetMapping("/{userName}")
    public ResponseEntity<Object> getCouponStorageByUserName(
            @PathVariable("userName") String userName,
            @RequestParam("pageCurrent") Integer pageCurrent,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sortOrder") String sortOrder,
            @RequestParam("sortBy") String sortBy
    ){
        Map<String,Object> result = new HashMap<>();
        System.out.println("123555");
        try {
            result.put("success",true);
            result.put("message","Get CouponStorage By UserName");
            result.put("data",couponStorageService.getCouponStorageByUserName(userName, pageCurrent, pageSize, sortOrder, sortBy));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userName}/yourCoupon")
    public ResponseEntity<Object> getCouponStorageByUserName(
            @PathVariable("userName") String userName
    ){
        Map<String,Object> result = new HashMap<>();
        System.out.println("123555");
        try {
            result.put("success",true);
            result.put("message","Get CouponStorage By UserName");
            result.put("data",couponStorageService.getAllCouponStorageByUserName(userName));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Object> createCoupon(
            @RequestPart("couponStorageRequest") CouponStorageRequest couponStorageRequest
            ){
        System.out.println("Claim Coupon ");
        System.out.println(couponStorageRequest);
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Add Coupon To CouponStorage ");
            result.put("data",couponStorageService.addCouponToCouponStorage(couponStorageRequest));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{couponStorageId}")
    public ResponseEntity<Object> deleteCouponStorageById(@PathVariable("couponStorageId") Integer couponStorageId){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Delete CouponStorage By Id ");
            result.put("data",couponStorageService.removeCouponInStorage(couponStorageId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }


    @GetMapping("/addCouponToStorage/{userName}")
    public ResponseEntity<Object> addCouponToStorage(
            @PathVariable("userName") String userName,
            @RequestParam(value = "code",required = false) String code
    ){
        System.out.println("Claim Coupon ");
        System.out.println(userName);
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Add Coupon To CouponStorage ");
            result.put("data",couponStorageService.addCouponToCouponStorage(userName, code));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

}
