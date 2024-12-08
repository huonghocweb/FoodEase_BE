package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.CouponStorageMapper;
import poly.foodease.Model.Entity.Coupon;
import poly.foodease.Model.Entity.CouponStorage;
import poly.foodease.Model.Request.CouponStorageRequest;
import poly.foodease.Model.Response.CouponStorageResponse;
import poly.foodease.Repository.CouponRepo;
import poly.foodease.Repository.CouponStorageRepo;
import poly.foodease.Repository.UserRepo;
import poly.foodease.Service.CouponStorageService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CouponStorageServiceImpl implements CouponStorageService {
    @Autowired
    private  CouponStorageRepo couponStorageRepo;
    @Autowired
    private CouponStorageMapper couponStorageMapper;
    @Autowired
    private CouponRepo couponRepo;
    @Autowired
    private UserRepo userRepo;

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'STAFF')")
    @Override
    public List<CouponStorageResponse> getAllCouponStorageByUserName(String userName) {
        List<CouponStorage> couponStorages = couponStorageRepo.getAllCouponStorageByUserName(userName);
        couponStorages.stream()
                .map(couponStorageMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return couponStorages.stream()
                .map(couponStorageMapper :: convertEnToRes)
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN', 'STAFF')")
    @Override
    public Page<CouponStorageResponse> getCouponStorageByUserName(String userName , Integer pageCurrent, Integer pageSize, String orderBy, String sortBy) {
        Sort sort = Sort.by(new Sort.Order(Objects.equals(orderBy, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC , sortBy));
        Pageable pageable = PageRequest.of(pageCurrent, pageSize, sort);
        Page<CouponStorage> couponStoragesPage= couponStorageRepo.getCouponStorageByUserName(userName , pageable);
        List<CouponStorageResponse> couponStorageResponses = couponStoragesPage.getContent().stream()
                .map(couponStorageMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(couponStorageResponses,pageable,couponStoragesPage.getTotalElements());
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN', 'STAFF')")
    @Override
    public Optional<CouponStorageResponse> getCouponStorageByCouponStorageId(Integer couponStorageId) {
        return Optional.empty();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'STAFF')")
    @Override
    public CouponStorageResponse addCouponToCouponStorage(CouponStorageRequest couponStorageRequest) {
        CouponStorage couponStorage = couponStorageMapper.convertReqToEn(couponStorageRequest);
        CouponStorage couponStorageCreated = couponStorageRepo.save(couponStorage);
        return couponStorageMapper.convertEnToRes(couponStorageCreated);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'STAFF')")
    @Override
    public CouponStorageResponse removeCouponInStorage(Integer couponStorageId) {
        CouponStorage couponStorage = couponStorageRepo.findById(couponStorageId)
                .orElseThrow(() -> new EntityNotFoundException("not found CouponStorage"));
        if(couponStorage != null){
            couponStorageRepo.delete(couponStorage);
        }
        return null;
    }


    @Override
    public CouponStorageResponse addCouponToCouponStorage(String userName, String code ) {
        CouponStorage couponStorage = new CouponStorage();
        Coupon coupon = couponRepo.findCouponByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Not found Coupon By Code"));
        couponStorage.setUser( userRepo.findUserByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException("not found User")));
        couponStorage.setStatus(true);
        if (coupon.getUsedCount() < coupon.getUseLimit()){
            System.out.println("Coupon: " + coupon.getCouponId());
            couponStorage.setCoupon(coupon);
            CouponStorage couponStorageCreated = couponStorageRepo.save(couponStorage);
            return couponStorageMapper.convertEnToRes(couponStorageCreated);
        }
        return null;
    }


}

