package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import poly.foodease.Model.Entity.Coupon;
import poly.foodease.Model.Request.CouponRequest;
import poly.foodease.Model.Response.CouponResponse;

@Mapper(componentModel = "spring")
public abstract class CouponMapper {

    public CouponResponse convertEnToResponse(Coupon coupon){
        return CouponResponse.builder()
                .couponId(coupon.getCouponId())
                .code(coupon.getCode())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .useLimit(coupon.getUseLimit())
                .usedCount(coupon.getUsedCount())
                .maxDiscountAmount(coupon.getMaxDiscountAmount())
                .description(coupon.getDescription())
                .discountPercent(coupon.getDiscountpercent())
                .imageUrl(coupon.getImageUrl() != null ?coupon.getImageUrl() : "" )
                .build();
    }

    public Coupon convertReqToEn(CouponRequest couponRequest){
        return Coupon.builder()
                .code(couponRequest.getCode())
                .description(couponRequest.getDescription())
                .usedCount(couponRequest.getUsedCount())
                .useLimit(couponRequest.getUseLimit())
                .startDate(couponRequest.getStartDate())
                .maxDiscountAmount(couponRequest.getMaxDiscountAmount())
                .endDate(couponRequest.getEndDate())
                .discountpercent(couponRequest.getDiscountPercent())
                .imageUrl(couponRequest.getImageUrl())
                .build();
    }

}
