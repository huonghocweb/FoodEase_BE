package poly.foodease.Service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Request.CouponRequest;
import poly.foodease.Model.Response.CouponResponse;

import java.util.Optional;

@Service
public interface CouponService {
    Page<CouponResponse> getAllCoupon(Integer pageCurrent , Integer pageSize, String orderBy, String sortBy);
    Optional<CouponResponse> getCouponResponseById(Integer couponId);
    CouponResponse createCoupon(CouponRequest couponRequest);
    Optional<CouponResponse> updateCoupon(Integer couponId, CouponRequest couponRequest);
    CouponResponse removeCoupon(Integer couponId);
    Optional<Object> checkCouponByCode(String code);
}
