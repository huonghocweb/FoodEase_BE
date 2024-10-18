package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {
    private String orderInfo;
    private Integer paymentStatus;
    private String totalPrice;
    private String paymentDateTime;
    private String transactionId;
}
