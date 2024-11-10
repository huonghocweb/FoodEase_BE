package poly.foodease.Mapper;

import java.util.Date;

import org.mapstruct.Mapper;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import poly.foodease.Model.Entity.FoodReview;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Response.FoodReviewResponse;

@Mapper(componentModel = "Spring")
public abstract class FoodReviewMapper {
public FoodReviewResponse converEnToRespon(FoodReview foodReview) {
	return FoodReviewResponse.builder()
	.reviewId(foodReview.getReviewId())
	.rating(foodReview.getRating())
	.review(foodReview.getReview())
	.reviewDate(foodReview.getReviewDate())
	.imageUrl(foodReview.getImageUrl())
	.userId(foodReview.getUserId())
	.foodId(foodReview.getFoodId())
	.user(foodReview.getUser())
	.build();
}
public FoodReview converResponToEn(FoodReviewResponse foodReviewResponse) {
	return FoodReview.builder()
			.reviewId(foodReviewResponse.getReviewId())
			.rating(foodReviewResponse.getRating())
			.review(foodReviewResponse.getReview())
			.reviewDate(foodReviewResponse.getReviewDate())
			.imageUrl(foodReviewResponse.getImageUrl())
			.userId(foodReviewResponse.getUserId())
			.foodId(foodReviewResponse.getFoodId())
			.user(foodReviewResponse.getUser())
			.build();
}
}
