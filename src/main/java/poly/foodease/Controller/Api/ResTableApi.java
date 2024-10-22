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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Response.ResTableResponse;
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
}
