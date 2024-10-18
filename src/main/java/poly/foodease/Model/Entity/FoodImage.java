package poly.foodease.Model.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="foods_image")
public class FoodImage {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int foodsImageId;
	private String images;
	private int foodId;
	
	@OneToMany (mappedBy = "foodImage")
	@JsonIgnore
	private List<Foods> foods;
}
