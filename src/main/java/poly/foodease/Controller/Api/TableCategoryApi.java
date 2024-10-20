package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.foodease.Model.Response.TableCategoryResponse;
import poly.foodease.Service.TableCategoryService;

@RestController
@RequestMapping("/api/tablecategories")
@CrossOrigin("*")
public class TableCategoryApi {

    @Autowired
    private TableCategoryService tableCategoryService;

    @GetMapping("get/{id}")
    public ResponseEntity<TableCategoryResponse> getTableCategoryById(@PathVariable("id") Integer tableCategoryId) {
        return ResponseEntity.ok(tableCategoryService.getTableCategoryById(tableCategoryId));
    }

    @GetMapping
    public ResponseEntity<List<TableCategoryResponse>> getAllTableCategory() {
        return ResponseEntity.ok(tableCategoryService.getAllTableCategory());
    }
}
