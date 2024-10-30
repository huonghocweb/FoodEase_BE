package poly.foodease.Model.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryAddressResponse {
        private Integer deliveryAddressId;
        private String deliveryAddressName;
        private Integer provinceId;
        private Integer districtId;
        private String wardCode;
        private String houseNumber;
        private String phoneAddress;
        private String fullAddress;
        private Boolean status;
}
