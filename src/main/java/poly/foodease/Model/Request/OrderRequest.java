package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private Double totalPrice;
    private Integer totalQuantity;
    private String deliveryAddress;
    private LocalDate orderDate;
    private LocalTime orderTime;
    private LocalDateTime paymentDateTime;
    private LocalDateTime estimatedDeliveryDateTime;
    private Integer shipFee;
    private Integer leadTime;
    private Integer couponId;
    private Integer paymentMethodId;
    private Integer shipMethodId;
    private Integer userId;
    private Integer orderStatusId;
   // private List<Integer> orderDetailsId;
}
