package poly.foodease.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import poly.foodease.Model.Entity.FoodReview;
import poly.foodease.Model.Request.FoodReviewRequest;
import poly.foodease.Model.Response.FoodReviewResponse;
import poly.foodease.Report.FoodRating;
import poly.foodease.Report.Rating;

import java.io.IOException;
import java.util.List;

@Service
public interface FoodReviewService {
	List<FoodReviewResponse> findFoodReviewByFoodId(Integer id);

	FoodReview createReview(FoodReviewRequest request) throws IOException;
	List<FoodReview>  findByFilter(Integer rating,Integer month,Integer year );
	FoodReviewResponse save(MultipartFile[] file,Integer rating,String review,Integer foodId,Integer userId);
	Rating AVGRating(Integer foodId);
	Page<FoodRating> FoodRating(Pageable page);
}
