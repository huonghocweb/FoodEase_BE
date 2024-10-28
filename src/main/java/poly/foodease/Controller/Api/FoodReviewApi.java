package poly.foodease.Controller.Api;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.foodease.Model.Entity.FoodReview;
import poly.foodease.Model.Request.FoodReviewRequest;
import poly.foodease.Model.Response.FoodReviewResponse;
import poly.foodease.Service.FoodReviewService;
import poly.foodease.Service.UploadFileService;
import poly.foodease.Utils.FileManageUtils;

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

	@PostMapping("/create")
	public ResponseEntity<FoodReview> createReview(@ModelAttribute FoodReviewRequest request) {
		try {
			FoodReview review = foodReviewService.createReview(request);
			return ResponseEntity.ok(review);
		} catch (IOException e) {
			return ResponseEntity.status(500).body(null);
		}
	}



}
