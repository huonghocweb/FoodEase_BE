package poly.foodease.Model.Response;

import java.util.Date;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodReviewResponse {
	private int reviewId;
	private int rating;
	private String review;
	private Date reviewDate=new Date();
	private String imageUrl;
	private int userId;
	private int foodId;
	private User user;
	private Foods food;
}
