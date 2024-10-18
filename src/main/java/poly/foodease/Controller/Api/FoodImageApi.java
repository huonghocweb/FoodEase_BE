package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poly.foodease.Model.Entity.FoodImage;
import poly.foodease.Model.Response.FoodImageResponse;
import poly.foodease.Service.FoodImageService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user/foodImage")
public class FoodImageApi {
@Autowired
FoodImageService foodImageService;

	@GetMapping("/findImage/{id}")	
	public ResponseEntity<List<FoodImageResponse>> find(@PathVariable ("id") Integer id)
	{		
		return ResponseEntity.ok(foodImageService.findFoodImageByFoodId(id));
	}
	
}
