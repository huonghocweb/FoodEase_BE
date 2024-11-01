package poly.foodease.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import poly.foodease.Model.Entity.FoodReview;
import poly.foodease.Model.Request.FoodReviewRequest;
import poly.foodease.Model.Response.FoodReviewResponse;

import java.io.IOException;
import java.util.List;

@Service
public interface FoodReviewService {
	List<FoodReviewResponse> findFoodReviewByFoodId(Integer id);
	FoodReviewResponse save(MultipartFile file,Integer rating,String review,Integer foodId);

	FoodReview createReview(FoodReviewRequest request) throws IOException;
	FoodReview save(FoodReview foodReview);
	List<FoodReview>  findByFilter(Integer rating,Integer month,Integer year );
}
