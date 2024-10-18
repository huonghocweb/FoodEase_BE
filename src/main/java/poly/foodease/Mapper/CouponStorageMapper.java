package poly.foodease.Mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import poly.foodease.Model.Entity.CouponStorage;
import poly.foodease.Model.Request.CouponStorageRequest;
import poly.foodease.Model.Response.CouponStorageResponse;
import poly.foodease.Repository.CouponRepo;
import poly.foodease.Repository.UserRepo;

@Mapper(componentModel = "spring")
public abstract class CouponStorageMapper {
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CouponRepo couponRepo;


    public CouponStorageResponse convertEnToRes(CouponStorage couponStorage){
        return CouponStorageResponse.builder()
                .couponStorageId(couponStorage.getCouponStorageId())
                .user(couponStorage.getUser() != null ? userMapper.convertEnToRes(couponStorage.getUser()) : null)
                .coupon(couponStorage.getCoupon() != null ? couponMapper.convertEnToResponse(couponStorage.getCoupon()) : null)
                .build();
    }

    public CouponStorage convertReqToEn(CouponStorageRequest couponStorageRequest){
        return CouponStorage.builder()
                .user(userRepo.findUserByUserName(couponStorageRequest.getUserName())
                        .orElseThrow( () -> new EntityNotFoundException("Not found User")))
                .coupon(couponRepo.findCouponByCode(couponStorageRequest.getCode())
                        .orElseThrow(() -> new EntityNotFoundException("Not found Coupon")))
                .build();
    }
}
