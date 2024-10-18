package poly.foodease.Model.Entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
	@ManyToOne @JoinColumn(name="foodId",insertable = false,updatable = false)
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
