package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.DeliveryAddressMapper;
import poly.foodease.Model.Entity.DeliveryAddress;
import poly.foodease.Model.Request.DeliveryAddressRequest;
import poly.foodease.Model.Response.DeliveryAddressResponse;
import poly.foodease.Repository.DeliveryAddressRepo;
import poly.foodease.Service.DeliveryAddressService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    @Autowired
    private DeliveryAddressRepo deliveryAddressRepo;
    @Autowired
    private DeliveryAddressMapper deliveryAddressMapper;


    @PreAuthorize("hasAnyRole('USER','ADMIN', 'STAFF')")
    @Override
    public Page<DeliveryAddressResponse> getAllDeliveryAddress(Pageable pageable) {
        Page<DeliveryAddress> deliveryAddressPage = deliveryAddressRepo.findAll(pageable);
        List<DeliveryAddressResponse>deliveryAddressResponses = deliveryAddressPage.getContent().stream()
                .map(deliveryAddressMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(deliveryAddressResponses,pageable,deliveryAddressPage.getTotalElements());
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN', 'STAFF')")
    @Override
    public Page<DeliveryAddressResponse> getDeliveryAddressByUserName(String userName, Pageable pageable) {
        Page<DeliveryAddress> deliveryAddressePage = deliveryAddressRepo.getDeliveryAddressByUserName(userName,  pageable);
        List<DeliveryAddressResponse> deliveryAddressResponses = deliveryAddressePage.getContent().stream()
                .map(deliveryAddressMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(deliveryAddressResponses, pageable, deliveryAddressePage.getTotalElements());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'STAFF')")
    @Override
    public Optional<DeliveryAddressResponse> getDeliveryAddressById(Integer deliveryAddressId) {
        DeliveryAddress deliveryAddress = deliveryAddressRepo.findById(deliveryAddressId)
                .orElseThrow(() -> new EntityNotFoundException("Not found DeliveryAddress"));
        return Optional.of(deliveryAddressMapper.convertEnToRes(deliveryAddress));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'STAFF')")
    @Override
    public DeliveryAddressResponse createDeliveryAddress(DeliveryAddressRequest deliveryAddressRequest) {
        DeliveryAddress deliveryAddress = deliveryAddressMapper.convertReqToEn(deliveryAddressRequest);
        DeliveryAddress deliveryAddressCreated = deliveryAddressRepo.save(deliveryAddress);
        return deliveryAddressMapper.convertEnToRes(deliveryAddressCreated);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'STAFF')")
    @Override
    public Optional<DeliveryAddressResponse> updateDeliveryAddress(Integer deliveryAddressId, DeliveryAddressRequest deliveryAddressRequest) {
        return Optional.of(deliveryAddressRepo.findById(deliveryAddressId).map(deliveryAddressExists -> {
            DeliveryAddress deliveryAddress = deliveryAddressMapper.convertReqToEn(deliveryAddressRequest);
            deliveryAddress.setDeliveryAddressId(deliveryAddressExists.getDeliveryAddressId());
            DeliveryAddress deliveryAddressUpdated = deliveryAddressRepo.save(deliveryAddress);
            return deliveryAddressMapper.convertEnToRes(deliveryAddressUpdated);
        }).orElseThrow(() -> new EntityNotFoundException("Not found DeliveryAddress")));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Optional<DeliveryAddressResponse> removerDeliveryAddress(Integer deliveryAddressId) {
        return Optional.of(deliveryAddressRepo.findById(deliveryAddressId).map(deliveryAddressExists -> {
            deliveryAddressExists.setStatus(false);
            DeliveryAddress deliveryAddressUpdated = deliveryAddressRepo.save(deliveryAddressExists);
            return deliveryAddressMapper.convertEnToRes(deliveryAddressUpdated);
        }).orElseThrow(() -> new EntityNotFoundException("Not found DeliveryAddress")));
    }
}
