package poly.foodease.Model.Entity;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="food_variation_toppings")
public class FoodVariationToppings implements Serializable{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int foodVariationToppingsId ;	
	
	private int foodVariationId;		
	private int toppingId;
	@ManyToOne @JoinColumn(name="foodVariationId",insertable = false,updatable = false)
	private FoodVariations foodVariations;
	@ManyToOne @JoinColumn(name="toppingId",insertable = false,updatable = false)
	 private Toppings toppings;
}
