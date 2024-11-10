package poly.foodease.Model.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FoodCategoriesReponse {
    private Integer foodCategoriesID;
    private String foodCategoriesName;

}
