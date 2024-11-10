package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequest {
    private String code;
    private String description;
    private Double discountPercent;
    private Double maxDiscountAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer useLimit;
    private Integer usedCount;
    private String imageUrl;
}
