package poly.foodease.Controller.Api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Entity.FoodCategories;
import poly.foodease.Model.Response.FoodCategoriesReponse;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Service.FoodCategoryService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/categories")
public class FoodCategoriesApi {

    @Autowired
    FoodCategoryService foodCategoryService;
    @GetMapping("/findAll")
   public ResponseEntity<List<FoodCategoriesReponse>> findAll(){
    	try {
    		List<FoodCategoriesReponse> list=foodCategoryService.findAll();
        	return ResponseEntity.ok(list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
    	
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id){
        foodCategoryService.deleteCategories(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/addFoodCategory")
    public ResponseEntity<?> createCategory(@RequestParam("categoryName") String name){
    	try {
    		FoodCategoriesReponse CategoriesReponse=foodCategoryService.save(name);
        	return ResponseEntity.ok(CategoriesReponse);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
    	
    }

}
