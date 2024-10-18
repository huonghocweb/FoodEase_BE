package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailsRequest {
    private Double price;
    private Integer quantity;
    private Integer orderId;
    private Integer foodVaId;
}
