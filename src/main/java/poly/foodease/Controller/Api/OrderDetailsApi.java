package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import poly.foodease.Model.Entity.OrderDetails;
import poly.foodease.Report.FoodBuyMost;
import poly.foodease.Report.FoodSold;
import poly.foodease.Repository.OrderDetailsRepo;
import poly.foodease.Repository.OrderRepo;
import poly.foodease.Service.OrderDetailsService;
import poly.foodease.Service.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/orderDetails")
public class OrderDetailsApi {
	@Autowired
	OrderDetailsService orderDetailsService;

	@Autowired
	private OrderService orderService;

	@Autowired
	OrderDetailsRepo orderDetailsRepository;
	@Autowired
	OrderDetailsRepo orderDetailsRepo;

	@Autowired
	OrderRepo orderRepository;

	@GetMapping("/orderDetailsHistory/{orderId}")
	public ResponseEntity<Object> getOrderDetailsByOrderId(@PathVariable("orderId") Integer orderId) {
		Map<String, Object> result = new HashMap<>();
		System.out.println("Get OrderDetails By OrderId");
		try {
			result.put("success", true);
			result.put("message", "Get OrderDetails by orderId");
			result.put("data", orderDetailsService.getOrderDetailsByOrderId(orderId));
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
			result.put("data", null);
		}
		return ResponseEntity.ok(result);
	}

	@GetMapping("/foodBuyMost")
	public ResponseEntity<Page<FoodBuyMost>> foodBuyMost(@RequestParam("page") Optional<Integer> page,
			@RequestParam(value = "sortBy", required = false, defaultValue = "countFood") String sortBy,
			@RequestParam(value = "sortDirection", required = false, defaultValue = "DESC") String sortDirection) {
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), 5,
					Sort.by(Sort.Direction.fromString(sortDirection), sortBy));

			Page<FoodBuyMost> list = orderDetailsService.FoodBuyMost(pageable);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.ok(null);
		}

	}

	@GetMapping("/findSold")
	public ResponseEntity<FoodBuyMost> sold(@RequestParam("foodId") Integer foodId) {
		try {
			FoodBuyMost list = orderDetailsService.FoodSold(foodId);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.ok(null);
		}

	}

	@GetMapping("/all")
	public ResponseEntity<List<OrderDetails>> all() {
		List<OrderDetails> all = orderDetailsRepo.findAll();
		return ResponseEntity.ok(all);
	}

	@GetMapping("/findSoldByFoodVariationId/{id}")
	public ResponseEntity<FoodSold> findSoldByFoodVariationId(@PathVariable("id") Integer id) {
		try {
			FoodSold foodSold = orderDetailsService.FoodSoldByFoodVariationId(id);
			return ResponseEntity.ok(foodSold);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.ok(null);
		}

	}
	@GetMapping("/foodSoldByFoodId/{id}")
	public ResponseEntity<FoodSold> foodSoldByFoodId(@PathVariable ("id") Integer id)
	{
		try {
			FoodSold foodSoldByFoodId = orderDetailsService.FoodSoldByFoodId(id);
			return ResponseEntity.ok(foodSoldByFoodId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.ok(null);
		}
	}
}
