package poly.foodease.Model.Request;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishListRequest {
    private Integer wishListId;
    private String wishListName;
    private Integer userId;
    private List<Integer> foodId = new ArrayList<>();
}
