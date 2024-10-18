package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poly.foodease.Model.Entity.Toppings;
import poly.foodease.Service.ToppingService;

@CrossOrigin("*")
@RestController
@RequestMapping("api/user/topping")
public class ToppingApi {
@Autowired
ToppingService toppingService;
	@GetMapping("/findAllTopping") 
	public ResponseEntity<List<Toppings>> findAllTopping ()
	{
		return ResponseEntity.ok(toppingService.findAll());
	}
}
