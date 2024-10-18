package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponStorageResponse {
    private Integer couponStorageId;
    private UserResponse user;
    private CouponResponse coupon;
}
