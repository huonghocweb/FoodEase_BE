package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Response.FoodResponse;
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
	public ResponseEntity<List<FoodResponse>> findAll()
	{
		List<FoodResponse> list = foodService.findAll();
		return ResponseEntity.ok(list);
	}

}
