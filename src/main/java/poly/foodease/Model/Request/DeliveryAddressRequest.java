package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryAddressRequest {
    private String deliveryAddressName;
    private Integer provinceId;
    private Integer districtId;
    private String wardCode;
    private String houseNumber;
    private String phoneAddress;
    private String fullAddress;
    private Boolean status;
    private Integer userId;
}
