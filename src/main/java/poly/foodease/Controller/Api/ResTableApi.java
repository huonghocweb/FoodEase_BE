package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
