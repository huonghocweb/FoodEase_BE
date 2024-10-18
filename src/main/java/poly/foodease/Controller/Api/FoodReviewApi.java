package poly.foodease.Controller.Api;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import poly.foodease.Model.Entity.FoodReview;
import poly.foodease.Model.Response.FoodReviewResponse;
import poly.foodease.Service.FoodReviewService;
import poly.foodease.Service.UploadFileService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user/foodReview")
public class FoodReviewApi {
	@Autowired
	FoodReviewService foodReviewService;
	@Autowired
	UploadFileService uploadFileService;
	@GetMapping("/findfoodReviewByFoodId/{id}")
	public ResponseEntity<List<FoodReviewResponse>> findfoodReviewByFoodId(@PathVariable ("id") Integer id)
	{
		List<FoodReviewResponse> list=foodReviewService.findFoodReviewByFoodId(id);
		return ResponseEntity.ok(list);
	}
	
	@PostMapping("/comment")
	ResponseEntity<?>postComment (@RequestParam("file") MultipartFile file,@RequestParam("rating") Integer rating,@RequestParam("review") String review,
			@RequestParam("foodId") Integer foodId)
	{
		try {
			FoodReviewResponse foodReviewResponse =foodReviewService.save(file, rating, review, foodId);
			return ResponseEntity.ok(foodReviewResponse);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("không thành công comment");
			
			e.printStackTrace();
			return null;
		}
	
		
	
	}

}
