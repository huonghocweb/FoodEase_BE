package poly.foodease.Mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import poly.foodease.Model.Entity.DeliveryAddress;
import poly.foodease.Model.Request.DeliveryAddressRequest;
import poly.foodease.Model.Response.DeliveryAddressResponse;
import poly.foodease.Repository.UserRepo;

@Mapper(componentModel = "spring")
public abstract class DeliveryAddressMapper {

    @Autowired
    UserRepo userRepo;

    public DeliveryAddressResponse convertEnToRes(DeliveryAddress deliveryAddress){
        return DeliveryAddressResponse.builder()
                .deliveryAddressId(deliveryAddress.getDeliveryAddressId())
                .deliveryAddressName(deliveryAddress.getDeliveryAddressName())
                .phoneAddress(deliveryAddress.getPhoneAddress())
                .houseNumber(deliveryAddress.getHouseNumber())
                .fullAddress(deliveryAddress.getFullAddress())
                .provinceId(deliveryAddress.getProvinceId())
                .districtId(deliveryAddress.getDistrictId())
                .wardCode(deliveryAddress.getWardCode())
                .status(deliveryAddress.getStatus())
                .build();
    }

    public DeliveryAddress convertReqToEn(DeliveryAddressRequest deliveryAddressRequest){
        return DeliveryAddress.builder()
                .deliveryAddressName(deliveryAddressRequest.getDeliveryAddressName())
                .houseNumber(deliveryAddressRequest.getHouseNumber())
                .phoneAddress(deliveryAddressRequest.getPhoneAddress())
                .provinceId(deliveryAddressRequest.getProvinceId())
                .fullAddress(deliveryAddressRequest.getFullAddress())
                .districtId(deliveryAddressRequest.getDistrictId())
                .wardCode(deliveryAddressRequest.getWardCode())
                .status(true)
                .user(deliveryAddressRequest.getUserId() != null ? userRepo.findById(deliveryAddressRequest.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("Not found Delivery Address")) : null)
                .build();
    }
}
