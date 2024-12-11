package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.CouponMapper;
import poly.foodease.Model.Entity.Coupon;
import poly.foodease.Model.Request.CouponRequest;
import poly.foodease.Model.Response.CouponResponse;
import poly.foodease.Repository.CouponRepo;
import poly.foodease.Service.CouponService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponRepo couponRepo;
    @Autowired
    CouponMapper couponMapper;


    @Override
    public Page<CouponResponse> getAllCoupon(Integer pageCurrent, Integer pageSize, String orderBy, String sortBy) {
        Sort sort = Sort.by(new Sort.Order(Objects.equals(orderBy, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC , sortBy));
        Pageable pageable = PageRequest.of( pageCurrent, pageSize, sort);
        Page<Coupon> couponsPage = couponRepo.findAll(pageable);
        List<CouponResponse> couponResponses = couponsPage.getContent().stream()
                .map(couponMapper :: convertEnToResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(couponResponses,pageable, couponsPage.getTotalElements());
    }


    @Override
    public Optional<CouponResponse> getCouponResponseById(Integer couponId) {
        Coupon coupon = couponRepo.findById(couponId)
                .orElseThrow(() -> new EntityNotFoundException("Not found Coupon"));
        return Optional.of(couponMapper.convertEnToResponse(coupon));
    }

    @Override
    public CouponResponse createCoupon(CouponRequest couponRequest) {
        Coupon coupon = couponMapper.convertReqToEn(couponRequest);
        Coupon couponCreated = couponRepo.save(coupon);
        return couponMapper.convertEnToResponse(couponCreated);
    }

    @Override
    public Optional<CouponResponse> updateCoupon(Integer couponId, CouponRequest couponRequest) {
        return Optional.of(couponRepo.findById(couponId)
                .map(couponExist -> {
                    Coupon coupon = couponMapper.convertReqToEn(couponRequest);
                    coupon.setCouponId(couponExist.getCouponId());
                    Coupon couponUpdated = couponRepo.save(coupon);
                    return couponMapper.convertEnToResponse(couponUpdated);
                })
                .orElseThrow(() -> new EntityNotFoundException("not found Coupon")));
    }

    @Override
    public CouponResponse removeCoupon(Integer couponId) {
        return null;
    }

    @Override
    public Optional<Object> checkCouponByCode(String code) {
        Coupon couponEn = couponRepo.findCouponByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Not found Coupon By Code"));
        CouponResponse coupon= couponMapper.convertEnToResponse(couponEn);
        System.out.println(coupon);
        Map<String,Object> map = new HashMap<>();
        if(coupon != null){
            LocalDate dateNow = LocalDate.now();
            if(coupon.getStartDate().isBefore(dateNow) && coupon.getEndDate().isAfter(dateNow) && coupon.getUsedCount() < coupon.getUseLimit()){
                map.put("accept",true);
                map.put("message","Use Coupon Success.");
                map.put("data",coupon);
            }else{
                map.put("accept",false);
                map.put("data",null);
                // ! datenow >= startDate
                if(! dateNow.isAfter(coupon.getStartDate())){
                    System.out.println("The Coupon is not start.");
                    map.put("message", "The Coupon isn't start.");
                    // ! datenow <= endDate
                }else if(! dateNow.isBefore(coupon.getEndDate())){
                    System.out.println("The Coupon has expired");
                    map.put("message","The Coupon has expired");
                    // usedCount < useLimit
                }else if(coupon.getUsedCount() >= coupon.getUseLimit()){
                    System.out.println("The Coupon is not use now");
                    map.put("message","The Coupon is out of use ");
                }

            }
        }
        return Optional.of(map);
    }
}
