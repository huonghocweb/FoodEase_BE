package poly.foodease.Controller.Api;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.foodease.Model.Request.CouponRequest;
import poly.foodease.Model.Response.CouponResponse;
import poly.foodease.Repository.CouponRepo;
import poly.foodease.Service.CloudinaryService;
import poly.foodease.Service.CouponService;
import poly.foodease.Utils.FileManageUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/coupon")
public class CouponApi {
    @Autowired
    CouponService couponService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public ResponseEntity<Object> getAllCoupon(
            @RequestParam("pageCurrent") Integer pageCurrent,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sortOrder") String sortOrder,
            @RequestParam("sortBy") String sortBy
    ){
        //System.out.println("Get All Coupon" + couponService.getAllCoupon(pageCurrent,pageSize,sortOrder,sortBy));
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get All Coupon");
            result.put("data",couponService.getAllCoupon(pageCurrent,pageSize,sortOrder,sortBy));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<Object> getCouponByCouponId(@PathVariable("couponId") Integer couponId){
        Map<String ,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get Coupon By CouponId");
            result.put("data",couponService.getCouponResponseById(couponId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }


    @PostMapping("/{folder}")
    public ResponseEntity<Object> createCoupon(
            @PathVariable("folder") String folder,
            @RequestPart("couponRequest")CouponRequest couponRequest,
            @RequestPart(value = "couponImage",required = false) MultipartFile[] files) throws IOException {
        Map<String,Object> result = new HashMap<>();
        couponRequest.setImageUrl("123");
        if(files != null){
            couponRequest.setImageUrl(cloudinaryService.uploadFile(files, folder).get(0));
//           couponRequest.setImageUrl(fileManageUtils.save(folder,files).get(0));
        }else{
            System.out.println("file null");
            couponRequest.setImageUrl(" ");
        }
        try {
            result.put("success",true);
            result.put("message","Create Coupon");
            result.put("data",couponService.createCoupon(couponRequest));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }


    @PutMapping("/{folder}/{couponId}")
    public ResponseEntity<Object> updateCoupon(
            @PathVariable("folder") String folder,
            @PathVariable("couponId") Integer couponId,
            @RequestPart("couponRequest") CouponRequest couponRequest,
            @RequestPart(value = "couponImage",required = false) MultipartFile[] files
    ) throws IOException {
        Map<String,Object> result = new HashMap<>();

        if(files == null){
            System.out.println("file null");
            CouponResponse couponResponse = couponService.getCouponResponseById(couponId)
                    .orElseThrow(() -> new EntityNotFoundException("Not Found Coupon"));
            couponRequest.setImageUrl(couponResponse.getImageUrl());
        }else{
            couponRequest.setImageUrl(cloudinaryService.uploadFile(files, folder).get(0));
        }
        System.out.println(couponRequest);
        try {
            result.put("success",true);
            result.put("message", "Update Coupon");
            result.put("data",couponService.updateCoupon(couponId , couponRequest));
        }catch (Exception e ){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{couponId}")
    public ResponseEntity<Object> removeCoupon(
            @PathVariable("couponId") Integer couponId
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message", "Delete Coupon");
            result.put("data",couponService.removeCoupon(couponId));
        }catch (Exception e ){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }


    @GetMapping("/checkCoupon/{code}")
    public ResponseEntity<Object> checkCouponByCode(@PathVariable("code") String code){
        Map<String,Object> result = new HashMap<>();
        System.out.println("Check COupon");
        System.out.println(couponService.checkCouponByCode(code));
        try {
            result.put("success",true);
            result.put("message", "Check Coupon By Code");
            result.put("data",couponService.checkCouponByCode(code));
        }catch (Exception e ){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }
}
