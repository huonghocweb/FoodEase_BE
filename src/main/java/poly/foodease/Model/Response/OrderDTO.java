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
public class OrderDTO {
    private String user;
    private String orderDate;
    private String deliveryAddress;
    private List<OrderDetailDTO> orderDetails;
}
