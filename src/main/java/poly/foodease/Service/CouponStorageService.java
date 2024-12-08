package poly.foodease.Service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.CouponStorage;
import poly.foodease.Model.Request.CouponStorageRequest;
import poly.foodease.Model.Response.CouponResponse;
import poly.foodease.Model.Response.CouponStorageResponse;

import java.util.List;
import java.util.Optional;

@Service
public interface CouponStorageService {
    List<CouponStorageResponse> getAllCouponStorageByUserName(String userName);
    Page<CouponStorageResponse> getCouponStorageByUserName(String userName, Integer pageCurrent, Integer pageSize, String orderBy, String sortBy);
    Optional<CouponStorageResponse> getCouponStorageByCouponStorageId(Integer couponStorageId);
    CouponStorageResponse addCouponToCouponStorage(CouponStorageRequest couponStorageRequest);
    CouponStorageResponse removeCouponInStorage(Integer couponStorageId);
    CouponStorageResponse addCouponToCouponStorage(String userName, String code );
}
