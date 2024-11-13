package poly.foodease.Model.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {
    private String foodName;
    private double price;
    private int quantity;
    private String size;
    private List<String> topping;
}
