package poly.foodease.Controller.Api;

import java.util.*;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Model.Response.FoodReviewResponse;
import poly.foodease.Repository.FoodsDao;
import poly.foodease.Service.FoodCategoryService;
import poly.foodease.Service.FoodsService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user/food")
public class FoodsApi {
	@Autowired
	FoodsService foodService;
	@Autowired
	FoodCategoryService foodCategoryService;
	@Autowired FoodsDao foodsDao;

//	@PostMapping
//	public ResponseEntity<FoodResponse> createFood(@RequestBody FoodRequest foodRequest) {
//		FoodResponse foodResponseDTO = foodService.createFood(foodRequest);
//		return new ResponseEntity<>(foodResponseDTO, HttpStatus.CREATED);
//	}
//
//	@GetMapping
//	public ResponseEntity<List<FoodResponse>> getAllFoods() {
//		List<FoodResponse> foods = foodService.getAllFoods();
//		return new ResponseEntity<>(foods, HttpStatus.OK);
//	}
//
//	@GetMapping("/{id}")
//	public ResponseEntity<FoodResponse> getFoodById(@PathVariable int id) {
//		return foodService.getFoodById(id)
//				.map(food -> new ResponseEntity<>(food, HttpStatus.OK))
//				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//	}
//
//	@PutMapping("/{id}")
//	public ResponseEntity<FoodResponse> updateFood(@PathVariable int id, @RequestBody FoodRequest foodRequest) {
//		FoodResponse updatedFood = foodService.updateFood(id, foodRequest);
//		return new ResponseEntity<>(updatedFood, HttpStatus.OK);
//	}
//
//	@DeleteMapping("/{id}")
//	public ResponseEntity<Void> deleteFood(@PathVariable int id) {
//		foodService.deleteFood(id);
//		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	}
	
	@GetMapping("/findMain")
	public ResponseEntity<Page<Foods>> findAll(@RequestParam("page") Optional<Integer> page)
	{
		Pageable pageable =PageRequest.of(page.orElse(0),5);
		Page<Foods> list = foodsDao.findAll(pageable);
		return ResponseEntity.ok(list);
	}
	@PostMapping("/addFood")
	ResponseEntity<?>postFood (@RequestParam(value = "file", required = false) MultipartFile[] file,@RequestParam("foodName") String foodName,
			@RequestParam("description") String description,@RequestParam("basePrice") Double basePrice,
			@RequestParam("discount") Integer discount,
			@RequestParam("categoriesId") Integer categoriesId)
	{
		try {
			
			
			FoodResponse foodResponse =foodService.save(foodName, description, basePrice, file, discount, categoriesId);
			
			return ResponseEntity.ok(foodResponse);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("không thành công comment");
			
			e.printStackTrace();
			return null;
		}

	}
	@GetMapping("foodAddFindAll")
	public ResponseEntity<Page<FoodResponse>> foodAddFindAll(@RequestParam("page") Optional<Integer> page)
	{
		try {
			Pageable pageable =PageRequest.of(page.orElse(0),5);
			Page<FoodResponse> list=foodService.findAddFoodAll(pageable);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Lỗi");
			return null;
		}
		
	}
	@DeleteMapping("/deleteFood/{foodId}")
	public ResponseEntity<Void> deleteFood(@PathVariable("foodId") Integer foodId){
		try {
			foodService.deleteFood(foodId);
			System.out.println("thanh cong");
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("thất bại");
			e.printStackTrace();
		}
		return null;
	}
	@PutMapping("/updateFood/{foodId}")
	public ResponseEntity<?> updateFood(
	        @PathVariable("foodId") Integer id,
	        @RequestParam(value = "file",required = false) MultipartFile[] file,
	        @RequestParam("foodName") String foodName,
	        @RequestParam("description") String description,
	        @RequestParam("basePrice") Double basePrice,
	        @RequestParam("discount") Integer discount,
	        @RequestParam("categoriesId") Integer categoriesId) {
	    
	    try {
	        // Update the food item with the provided ID
	        FoodResponse foodResponse = foodService.update(id, foodName, description, basePrice, file, discount, categoriesId);
	        
	        return ResponseEntity.ok(foodResponse);
	    } catch (Exception e) {
	        System.out.println("Cập nhật không thành công");
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Update failed");
	    }
	}
	


	// Hưởng
	@GetMapping("/getAllFoodByHuong")
	public ResponseEntity<Object> getAllFoods(
			@RequestParam("pageCurrent") Integer pageCurrent,
			@RequestParam("pageSize") Integer pageSize,
			@RequestParam("sortOrder") String sortOrder,
			@RequestParam("sortBy") String sortBy
	){
		Map<String,Object> result = new HashMap<>();
		System.out.println("Get Food By Huong");
		Sort sort = Sort.by(new Sort.Order(Objects.equals("asc", sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC , sortBy));
		Pageable pageable = PageRequest.of(pageCurrent,  pageSize, sort);
		try {
			result.put("success",true);
			result.put("message","Fill All Foods By Huong ");
			result.put("data",foodService.fillAllFoodByHuong(pageable));
		}catch (Exception e){
			result.put("success",false);
			result.put("message",e.getMessage());
			result.put("data",null);
		}
		return ResponseEntity.ok(result);
	}
}
