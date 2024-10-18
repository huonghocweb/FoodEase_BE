package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderReturnResponse {
    private Integer orderReturnId;
    private String reason;
    private LocalDateTime returnDateTime;
    private String status;
    private OrderResponse order;
    private FoodVariationResponse foodVariation;

}