package poly.foodease.Controller.Api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poly.foodease.Model.Entity.FoodVariationToppings;
import poly.foodease.Model.Response.FoodVariationToppingResponse;
import poly.foodease.Repository.FoodVariatonToppingDao;
import poly.foodease.Service.FoodVariationToppingService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user/topping")
public class FoodVariationToppingApi {

	@Autowired
	FoodVariationToppingService foodVariationToppingService;

	@GetMapping("/findVariationTopping/{id}")
	public ResponseEntity<List<FoodVariationToppingResponse>> findVariationTopping(@PathVariable("id") Integer id) {
		List<FoodVariationToppingResponse> list = foodVariationToppingService.findFoodVariationToppingById(id);
		return ResponseEntity.ok(list);
	}

	@PostMapping("/create")
	public ResponseEntity<List<FoodVariationToppingResponse>> save(
			@RequestBody List<FoodVariationToppingResponse> requests) {
		
		try {
			List<FoodVariationToppingResponse> responses = new ArrayList();
			 for (FoodVariationToppingResponse request : requests) {
			        FoodVariationToppingResponse response = foodVariationToppingService.Save(request.getFoodVariationId(), request.getToppingId());
			        responses.add(response);
			    }
		   
			 System.out.println("thêm topping thành công");
		    return ResponseEntity.ok(responses);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("thêm topping thất bại ");
			System.out.println(requests);
			e.printStackTrace();
			return ResponseEntity.ok(null);
		}
		
	}
}
