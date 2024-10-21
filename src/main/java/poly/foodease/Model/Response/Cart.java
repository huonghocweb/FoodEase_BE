package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private Integer cartId;
    private Map<Integer,CartItem> items = new HashMap<>();
   
}
