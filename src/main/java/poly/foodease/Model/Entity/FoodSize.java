package poly.foodease.Model.Entity;

import java.io.Serializable;
import java.util.List;

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
@Table(name="food_size")
public class FoodSize implements Serializable {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int foodSizeId;
	private String foodSizeName;
	private int price;
	@OneToMany (mappedBy = "foodSize")
	@JsonIgnore
	private List<FoodVariations> foodVariations;
}
