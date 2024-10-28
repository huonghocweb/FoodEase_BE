package poly.foodease.Model.Entity;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="food_review")
public class FoodReview {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reviewId;
	private int rating;
	private String review;
	private Date reviewDate=new Date();
	private String imageUrl;
	private int userId;
	private int foodId;

	@ManyToOne
	@JoinColumn(name="userId",insertable = false,updatable = false)
	@JsonManagedReference
	private User user;

	@ManyToOne
	@JoinColumn(name="foodId",insertable = false,updatable = false)
	@JsonManagedReference
	private Foods food;

}
