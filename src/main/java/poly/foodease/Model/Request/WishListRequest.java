package poly.foodease.Model.Request;

import java.util.List;

import lombok.Data;

@Data
public class WishListRequest {
    private String wishListName;
    private Integer userId;
    private List<Integer> foodIds;
    private List<String> foods;
    private Integer foodVariationId;  
    private Integer quantityStock;    
}
