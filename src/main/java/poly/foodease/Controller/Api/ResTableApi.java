package poly.foodease.Controller.Api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Response.ResTableResponse;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Service.CloudinaryService;
import poly.foodease.Service.ResTableService;

@RestController
@RequestMapping("/api/restables")
@CrossOrigin("*")
public class ResTableApi {


    @Autowired
    private ResTableService resTableService;
    @Autowired
    private CloudinaryService cloudinaryService;

    // @PostMapping
    // public ResponseEntity<ResTableResponse> createResTable(@RequestBody
    // ResTableRequest resTableRequest) {
    // return ResponseEntity.ok(resTableService.createResTable(resTableRequest));
    // }

    @PostMapping("/{folder}")
    public ResponseEntity<Object> createResTable(
            @PathVariable("folder") String folder,
            @RequestPart("resTableRequest") ResTableRequest resTableRequest,
            @RequestPart(value = "ImgResTable", required = false) MultipartFile[] files) throws IOException {
        Map<String, Object> result = new HashMap<>();
        resTableRequest.setImageUrl("123");
        if (files != null) {
            resTableRequest.setImageUrl(cloudinaryService.uploadFile(files, folder).get(0));
            // couponRequest.setImageUrl(fileManageUtils.save(folder,files).get(0));
        } else {
            System.out.println("file null");
            resTableRequest.setImageUrl(" ");
        }
        try {
            result.put("success", true);
            result.put("message", "Create Coupon");
            result.put("data", resTableService.createResTable(resTableRequest));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    // @PutMapping("update/{id}")
    // public ResponseEntity<ResTableResponse> updateResTable(@PathVariable("id")
    // Integer tableId,
    // @RequestBody ResTableRequest resTableRequest) {
    // return ResponseEntity.ok(resTableService.updateResTable(tableId,
    // resTableRequest));
    // }

    @PutMapping("/{folder}/{id}")
    public ResponseEntity<Object> updateResTable(
            @PathVariable("folder") String folder,
            @PathVariable("id") Integer tableId,
            @RequestPart("resTableRequest") ResTableRequest resTableRequest,
            @RequestPart(value = "ImgResTable", required = false) MultipartFile[] files) throws IOException {
        Map<String, Object> result = new HashMap<>();

        if (files == null) {
            System.out.println("file null");
            ResTableResponse resTableResponse = resTableService.getResTableByIdNew(tableId)
                    .orElseThrow(() -> new EntityNotFoundException("Not Found Coupon"));
            resTableRequest.setImageUrl(resTableRequest.getImageUrl());
        } else {
            resTableRequest.setImageUrl(cloudinaryService.uploadFile(files, folder).get(0));
        }
        System.out.println(resTableRequest);
        try {
            result.put("success", true);
            result.put("message", "Update Coupon");
            result.put("data", resTableService.updateResTableNew(tableId, resTableRequest));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    // @GetMapping("get/{id}")
    // public ResponseEntity<ResTableResponse> getResTableById(@PathVariable("id")
    // Integer tableId) {
    // return ResponseEntity.ok(resTableService.getResTableByIdNew(tableId));
    // }

    @GetMapping("get/{id}")
    public ResponseEntity<Object> getResTableById(@PathVariable("id") Integer tableId) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "Get ResTable By ResTableId");
            result.put("data", resTableService.getResTableByIdNew(tableId));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteResTable(@PathVariable("id") Integer tableId) {
        resTableService.deleteResTable(tableId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ResTableResponse>> getAllResTables() {
        return ResponseEntity.ok(resTableService.getAllResTables());
    }

    @GetMapping("/getResTableAvailable")
    public ResponseEntity<Object> getResTableAvailable(
            @RequestParam(value = "tableCategoryId" ,required = false) Integer tableCategoryId,
            @RequestParam(value = "capacity" ,required = false) Integer capacity,
            @RequestParam(value = "checkinDate", required = false)LocalDate checkinDate,
            @RequestParam(value = "checkinTime" ,required = false)LocalTime checkinTime,
            @RequestParam("pageCurrent") Integer pageCurrent,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sortOrder") String sortOrder,
            @RequestParam("sortBy") String sortBy
            ){
        Map<String,Object> result = new HashMap<>();
        Sort sort= Sort.by(new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Pageable pageable = PageRequest.of(pageCurrent, pageSize, sort);
        try {
            result.put("success",true);
            result.put("message","Get Res Table Available");
            if (tableCategoryId != null && capacity != null) {
                System.out.println(tableCategoryId);
                System.out.println(capacity);
                System.out.println("1");
                System.out.println(resTableService.getResTableByCapaAndCate(tableCategoryId, capacity , pageable).getContent());
                result.put("data", resTableService.getResTableByTableCategory(tableCategoryId , pageable));
              //  result.put("data", resTableService.getResTableByCapaAndCate(tableCategoryId, capacity , pageable));
            }
            else if (tableCategoryId != null) {
                System.out.println("2");
                result.put("data", resTableService.getResTableByTableCategory(tableCategoryId , pageable));
            }
            else if (capacity != null) {
                System.out.println("3");
                result.put("data", resTableService.getResTableByCapacity(capacity , pageable));
            }else {
                result.put("data", resTableService.getAllResTable(pageable));
            }
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checkResTableAvailable")
    public ResponseEntity<Object> checkResTableAvailable(
            @RequestParam("tableId") Integer tableId,
            @RequestParam("date") LocalDate date,
            @RequestParam("checkinTime") LocalTime checkinTime,
            @RequestParam("checkoutTime") LocalTime checkoutTime,
            @RequestParam("userId")Integer userId
            ) {
        Map<String, Object> result = new HashMap<>();
        System.out.println("checkResTableAvailable");
        System.out.println(tableId);
        System.out.println(date);
        System.out.println(checkinTime);
        System.out.println(checkoutTime);
        System.out.println(userId);
        try {
            result.put("success", true);
            result.put("message", "Check ResTable Available");
            result.put("data", resTableService.checkResTableInReservation(userId,tableId, date, checkinTime, checkoutTime));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }
}
