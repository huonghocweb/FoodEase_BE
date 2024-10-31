package poly.foodease.Controller.Api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Response.FoodVariationResponse;
import poly.foodease.Repository.FoodVariationsDao;
import poly.foodease.Service.FoodVariationsService;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/user/foodvariation")
public class FoodVariationsApi {
	
	@Autowired
	FoodVariationsService foodVariationsService;
	@Autowired
	FoodVariationsDao dao;
	
//	@GetMapping("/findFoodVariationByMainDishes")
//	public ResponseEntity<Page<FoodVariations>> findFoodVariationByMainDishes(@RequestParam("page")Optional<Integer>  page)
//	{
//		Pageable pageable = PageRequest.of(page.orElse(0), 3);
//		Page<FoodVariations> ListfoodVariations = dao.findByCategoryMainDishes(pageable);
//		return ResponseEntity.ok(ListfoodVariations);
//	}
	
	@GetMapping("/findFoodVariationByMainDishes")
	public ResponseEntity<Page<FoodVariationResponse>> findFoodVariationByMainDishes(@RequestParam("page")Optional<Integer>  page)
	{
		Pageable pageable = PageRequest.of(page.orElse(0), 3);
		Page<FoodVariationResponse> ListfoodVariations = foodVariationsService.findByCategoryMainDishes(pageable);
		return ResponseEntity.ok(ListfoodVariations);
	}
	@GetMapping("/findFoodVariationByDrink")
	public ResponseEntity<List<FoodVariationResponse>> findFoodVariationByDrink()
	{
		List<FoodVariationResponse> ListfoodVariations = foodVariationsService.findByCategoryDrink();
		return ResponseEntity.ok(ListfoodVariations);
	}
	@GetMapping("/findVaritionById/{id}")
	public ResponseEntity<FoodVariationResponse> findVariationById (@PathVariable ("id") Integer id)
	{
		return ResponseEntity.ok(foodVariationsService.findById1(id).get());
	}

	@GetMapping("/findFoodVariationBySize")
	public ResponseEntity<FoodVariationResponse> findFoodSizeBySize(@RequestParam Integer id,@RequestParam String sizeName)
	{
		FoodVariationResponse foodVariationsResponse = foodVariationsService.findFoodVariationBySize(id,sizeName);
		return ResponseEntity.ok(foodVariationsResponse);
	}
	@GetMapping("/findFoodVariationByFoodName")
	public ResponseEntity<List<FoodVariationResponse>> findFoodVariationByFoodName(@RequestParam String foodName){
		List<FoodVariationResponse> list=foodVariationsService.findFoodVariationByFoodName(foodName);
		return ResponseEntity.ok(list);
	}
	@GetMapping("/findFoodVariationByCategoryId/{id}")
	public ResponseEntity<List<FoodVariationResponse>> findFoodVariationByCategoryId(@PathVariable("id") Integer id){
		List<FoodVariationResponse> findFoodVariationByCategoryId = foodVariationsService.findFoodVariationByCategoryId(id);
		return ResponseEntity.ok(findFoodVariationByCategoryId);
	}

	 // Ngoc
	 @GetMapping("/findFoodVariationByUserId/{userId}")
	 public ResponseEntity<List<FoodVariationResponse>> findFoodVariationByUserId(@PathVariable("userId") Integer userId){

		 List<FoodVariationResponse> list=foodVariationsService.findFoodVariationByUserId(userId);
		 return ResponseEntity.ok(list);
	 }
	@GetMapping("/findAll")
	public ResponseEntity<Page<FoodVariationResponse>> findAll(@RequestParam("page") Optional<Integer>  page)
	{
		Pageable pageable = PageRequest.of(page.orElse(0), 5);
		Page<FoodVariationResponse> list=foodVariationsService.findAll(pageable);
		return ResponseEntity.ok(list);
	}
	@GetMapping("/findFoodVariationByFoodId/{foodId}")
	public ResponseEntity<List<FoodVariationResponse>>findFoodVariationByFoodId(@PathVariable("foodId") Integer id){
		List<FoodVariationResponse> list = foodVariationsService.findFoodVariationByFoodId(id);
		return ResponseEntity.ok(list);
	}
	@PostMapping("/AddFoodVariation")
	public ResponseEntity<?> Save(@RequestParam("foodId") Integer id,@RequestParam("file") MultipartFile[] file,
			@RequestParam("quantityStock") Integer quantityStock,@RequestParam("FoodSizeId") Integer FoodSizeId)
	{
		try {
			FoodVariationResponse foodVariationResponse=foodVariationsService.Save(file, quantityStock, FoodSizeId, id);
			System.out.println("Thêm  thành công foodVariation");
			return ResponseEntity.ok(foodVariationResponse);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Thêm không thành công foodVariation");
			return null;
		}
	}
	@DeleteMapping("/deleteFoodVariation/{foodIdVariationId}")
	public ResponseEntity<Void> deleteFoodVariation(@PathVariable("foodIdVariationId") Integer id){
		try {
			foodVariationsService.deletebyId(id);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
	}
	@PutMapping("/updateFoodVariation/{foodVariationId}")
	public ResponseEntity<?> Update(@RequestParam("foodId") Integer id,@RequestParam("file") MultipartFile[] file,
			@RequestParam("quantityStock") Integer quantityStock,@RequestParam("FoodSizeId") Integer FoodSizeId,@PathVariable
			("foodVariationId") Integer foodVariationId)
	{
		try {
			FoodVariationResponse foodVariationResponse=foodVariationsService.Update(foodVariationId,file, quantityStock, FoodSizeId, id);
			System.out.println("Update  thành công foodVariation");
			return ResponseEntity.ok(foodVariationResponse);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Udpate không thành công foodVariation");
			return null;
		}
	}
	
}
