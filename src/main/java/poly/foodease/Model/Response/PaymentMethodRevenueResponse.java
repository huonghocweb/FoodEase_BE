package poly.foodease.Model.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentMethodRevenueResponse {
    private String paymentMethodName;
    private Double totalRevenue;
    private Long userCount;
    private Long orderCount;
}
