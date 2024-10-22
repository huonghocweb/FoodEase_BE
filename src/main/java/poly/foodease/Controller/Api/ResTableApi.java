package poly.foodease.Controller.Api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Response.ResTableResponse;
import poly.foodease.Service.ResTableService;

@RestController
@RequestMapping("/api/restables")
@CrossOrigin("*")
public class ResTableApi {


    @Autowired
    private ResTableService resTableService;

    @PostMapping
    public ResponseEntity<ResTableResponse> createResTable(@RequestBody ResTableRequest resTableRequest) {
        return ResponseEntity.ok(resTableService.createResTable(resTableRequest));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ResTableResponse> updateResTable(@PathVariable("id") Integer tableId,
            @RequestBody ResTableRequest resTableRequest) {
        return ResponseEntity.ok(resTableService.updateResTable(tableId, resTableRequest));
    }

    @GetMapping("get/{id}")
    public ResponseEntity<ResTableResponse> getResTableById(@PathVariable("id") Integer tableId) {
        return ResponseEntity.ok(resTableService.getResTableById(tableId));
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

}
