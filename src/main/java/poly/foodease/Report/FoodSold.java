package poly.foodease.Report;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Entity.Foods;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class FoodSold {
@Id
private Long sold;
private Integer FoodVariationId;

}
