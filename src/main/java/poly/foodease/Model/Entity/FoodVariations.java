package poly.foodease.Model.Entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="food_variations")
public class FoodVariations implements Serializable{

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int foodVariationId;
	private String imageUrl;
	private Date createdAt;
	private Date updatedAt;
	private int quantityStock;
	private int foodId;
	private int foodSizeId;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="foodId",insertable = false,updatable = false)
	private Foods food;
	
	@ManyToOne @JoinColumn(name="foodSizeId",insertable = false,updatable = false)
	private FoodSize foodSize;
	
	@OneToMany (mappedBy = "foodVariations")
	@JsonIgnore
	private List<FoodVariationToppings> foodVariationToppings;

	@OneToMany(mappedBy = "foodVariations")
	@JsonIgnore
	private List<OrderReturn> orderReturns;

	@OneToMany(mappedBy = "foodVariations")
	@JsonIgnore
	private List<OrderDetails> orderDetails;


}
