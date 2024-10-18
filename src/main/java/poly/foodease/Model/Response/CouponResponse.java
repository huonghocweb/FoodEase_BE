package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponResponse {
    private Integer couponId;
    private String code;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer useLimit;
    private Integer usedCount;
    private Double discountPercent;
    private Double maxDiscountAmount;
    private String imageUrl;

}
