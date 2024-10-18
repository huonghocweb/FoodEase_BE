package poly.foodease.Controller.Api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Entity.FoodCategories;
import poly.foodease.Model.Response.FoodCategoriesReponse;
import poly.foodease.Service.FoodCategoryService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/categories")
public class FoodCategoriesApi {

    @Autowired
    FoodCategoryService foodCategoryService;

    @GetMapping
    public List<FoodCategories> getIndex(){
        return foodCategoryService.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id){
        foodCategoryService.deleteCategories(id);
        return ResponseEntity.noContent().build();
    }

}
