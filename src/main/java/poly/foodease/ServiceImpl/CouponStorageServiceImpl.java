package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.CouponStorageMapper;
import poly.foodease.Model.Entity.CouponStorage;
import poly.foodease.Model.Request.CouponStorageRequest;
import poly.foodease.Model.Response.CouponStorageResponse;
import poly.foodease.Repository.CouponStorageRepo;
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

    @Override
    public Optional<CouponStorageResponse> getCouponStorageByCouponStorageId(Integer couponStorageId) {
        return Optional.empty();
    }

    @Override
    public CouponStorageResponse addCouponToCouponStorage(CouponStorageRequest couponStorageRequest) {
        CouponStorage couponStorage = couponStorageMapper.convertReqToEn(couponStorageRequest);
        CouponStorage couponStorageCreated = couponStorageRepo.save(couponStorage);
        return couponStorageMapper.convertEnToRes(couponStorageCreated);
    }

    @Override
    public CouponStorageResponse removeCouponInStorage(Integer couponStorageId) {
        CouponStorage couponStorage = couponStorageRepo.findById(couponStorageId)
                .orElseThrow(() -> new EntityNotFoundException("not found CouponStorage"));
        if(couponStorage != null){
            couponStorageRepo.delete(couponStorage);
        }
        return null;
    }
}
