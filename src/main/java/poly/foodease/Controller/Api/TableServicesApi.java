package poly.foodease.Controller.Api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Model.Request.TableServicesRequest;
import poly.foodease.Model.Response.TableServicesResponse;
import poly.foodease.Service.CloudinaryService;
import poly.foodease.Service.TableServicesService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/tableService")
public class TableServicesApi {
    @Autowired
    TableServicesService tableServicesService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/fill")
    public ResponseEntity<List<TableServicesResponse>> getAllTableServicesChanh() {
        return ResponseEntity.ok(tableServicesService.getAllTableServicesChanh());
    }

    @GetMapping
    public ResponseEntity<Object> getAllTableServices(
            @RequestParam("pageCurrent") Integer pageCurrent,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sortOrder") String sortOrder,
            @RequestParam("sortBy") String sortBy) {
        Map<String, Object> result = new HashMap<>();
        System.out.println(tableServicesService.getAllTableServices(pageCurrent, pageSize, sortOrder, sortBy));
        try {
            result.put("success", true);
            result.put("message", "Get All Services");
            result.put("data", tableServicesService.getAllTableServices(pageCurrent, pageSize, sortOrder, sortBy));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{tableServicesId}")
    public ResponseEntity<Object> getTableServicesById(
            @PathVariable("tableServicesId") Integer tableServicesId) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "Get Table Services By Id");
            result.put("data", tableServicesService.getTableServicesById(tableServicesId));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{folder}")
    public ResponseEntity<Object> createTableServices(
            @PathVariable("folder") String folder,
            @RequestPart("tableServicesRequest") TableServicesRequest tableServicesRequest,
            @RequestPart(value = "ImgService", required = false) MultipartFile[] files) throws IOException {
        Map<String, Object> result = new HashMap<>();
        tableServicesRequest.setImageUrl("123");
        if (files != null) {
            tableServicesRequest.setImageUrl(cloudinaryService.uploadFile(files, folder).get(0));
            // couponRequest.setImageUrl(fileManageUtils.save(folder,files).get(0));
        } else {
            System.out.println("file null");
            tableServicesRequest.setImageUrl(" ");
        }
        try {
            result.put("success", true);
            result.put("message", "Create Coupon");
            result.put("data", tableServicesService.createTableServices(tableServicesRequest));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{folder}/{tableServicesId}")
    public ResponseEntity<Object> updateTableServices(
            @PathVariable("folder") String folder,
            @PathVariable("tableServicesId") Integer tableServicesId,
            @RequestPart("tableServicesRequest") TableServicesRequest tableServicesRequest,
            @RequestPart(value = "ImgService", required = false) MultipartFile[] files) throws IOException {
        Map<String, Object> result = new HashMap<>();

        if (files == null) {
            System.out.println("file null");
            TableServicesResponse tableServicesResponse = tableServicesService.getTableServicesById(tableServicesId)
                    .orElseThrow(() -> new EntityNotFoundException("Not Found Coupon"));
            tableServicesRequest.setImageUrl(tableServicesRequest.getImageUrl());
        } else {
            tableServicesRequest.setImageUrl(cloudinaryService.uploadFile(files, folder).get(0));
        }
        System.out.println(tableServicesRequest);
        try {
            result.put("success", true);
            result.put("message", "Update Coupon");
            result.put("data", tableServicesService.updateTableServices(tableServicesId, tableServicesRequest));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("delete/{tableServicesId}")
    public ResponseEntity<Void> deleteTableServices(@PathVariable("tableServicesId") Integer tableServicesId) {
        tableServicesService.deleteTableServices(tableServicesId);
        return ResponseEntity.noContent().build();
    }
}
