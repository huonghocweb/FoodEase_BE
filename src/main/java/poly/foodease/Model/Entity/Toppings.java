package poly.foodease.Model.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="toppings")
public class Toppings implements Serializable{
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int toppingId;
	private String toppingName;
	private double price;
	
	@OneToMany (mappedBy = "toppings")
	@JsonIgnore
	private Set<FoodVariationToppings> foodVariationToppings;
}
