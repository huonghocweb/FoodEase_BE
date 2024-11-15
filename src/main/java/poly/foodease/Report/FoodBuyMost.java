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
public class FoodBuyMost {
@Id
private Long countFood;
private Foods foods;
}
