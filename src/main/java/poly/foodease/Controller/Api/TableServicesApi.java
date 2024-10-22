package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Service.TableServicesService;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/tableService")
public class TableServicesApi {
    @Autowired
    TableServicesService tableServicesService;

    @GetMapping
    public ResponseEntity<Object> getAllTableServices(
            @RequestParam("pageCurrent") Integer pageCurrent,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sortOrder") String sortOrder,
            @RequestParam("sortBy") String sortBy
    ){
        Map<String,Object> result = new HashMap<>();
        System.out.println(tableServicesService.getAllTableServices(pageCurrent, pageSize, sortOrder, sortBy));
        try {
            result.put("success",true);
            result.put("message","Get All Services");
            result.put("data",tableServicesService.getAllTableServices(pageCurrent, pageSize, sortOrder, sortBy));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
            return ResponseEntity.ok(result);
    }

    @GetMapping("/{tableServicesId}")
    public ResponseEntity<Object> getTableServicesById(
            @PathVariable("tableServicesId") Integer tableServicesId
    ){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get Table Services By Id");
            result.put("data",tableServicesService.getTableServicesById(tableServicesId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }
}
