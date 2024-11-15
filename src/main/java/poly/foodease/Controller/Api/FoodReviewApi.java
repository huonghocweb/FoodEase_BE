package poly.foodease.Controller.Api;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.foodease.Model.Entity.FoodReview;
import poly.foodease.Model.Request.FoodReviewRequest;
import poly.foodease.Model.Response.FoodReviewResponse;
import poly.foodease.Report.FoodRating;
import poly.foodease.Report.Rating;
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
	ResponseEntity<?>postComment (@RequestParam("file") MultipartFile[] file,@RequestParam("rating") Integer rating,@RequestParam("review") String review,
			@RequestParam("foodId") Integer foodId,@RequestParam("userId") Integer userId)
	{
		try {
			FoodReviewResponse foodReviewResponse =foodReviewService.save(file, rating, review, foodId,userId);
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



	@GetMapping("/AvgRating")
	public ResponseEntity<Rating> AvgRating(@RequestParam("foodId") Integer foodId){
		Rating rating = foodReviewService.AVGRating(foodId);
		return ResponseEntity.ok(rating);
	}
	@GetMapping("/FoodRating")
	public ResponseEntity<Page<FoodRating>> FoodRating(@RequestParam("page") Optional<Integer> page
			,@RequestParam(value = "sortBy", required = false, defaultValue = "avgRating") String sortBy,
	        @RequestParam(value = "sortDirection", required = false, defaultValue = "DESC") String sortDirection)
	{
		try {
			
			
			Pageable pageable = PageRequest.of(page.orElse(0), 5,Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
			Page<FoodRating> list = foodReviewService.FoodRating(pageable);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.ok(null);
		}
	}
	
}
