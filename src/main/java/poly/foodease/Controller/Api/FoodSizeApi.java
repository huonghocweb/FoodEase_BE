package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import poly.foodease.Model.Entity.FoodSize;
import poly.foodease.Model.Response.FoodSizeResponse;
import poly.foodease.Service.FoodSizeService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user/foodSize")
public class FoodSizeApi {
	@Autowired
	FoodSizeService foodSizeService;
	@GetMapping("/findFoodSizeBySize")
	public ResponseEntity<FoodSizeResponse> findFoodSizeBySize(@RequestParam String sizeName)
	{
		FoodSizeResponse foodSize = foodSizeService.findFoodSizeBySize(sizeName);
		return ResponseEntity.ok(foodSize);
	}
	@GetMapping("/findAllFoodSize")
	public ResponseEntity<List<FoodSizeResponse>> findAllFoodSize()
	{
		return ResponseEntity.ok(foodSizeService.findAll());
	}
	@GetMapping("/findFoodSizeByFoodId/{foodId}")
	public ResponseEntity<List<FoodSizeResponse>> findFoodSizeByFoodId( @PathVariable("foodId") Integer foodId)
	{
		List<FoodSizeResponse> list =foodSizeService.findFoodSizeByFoodId(foodId);
		return ResponseEntity.ok(list);
	}
	

}
