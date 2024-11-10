package poly.foodease.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Request.DeliveryAddressRequest;
import poly.foodease.Model.Response.DeliveryAddressResponse;

import java.util.Optional;

@Service
public interface DeliveryAddressService {
    Page<DeliveryAddressResponse> getAllDeliveryAddress(Pageable pageable);
    Page<DeliveryAddressResponse> getDeliveryAddressByUserName(String userName, Pageable pageable);
    Optional<DeliveryAddressResponse> getDeliveryAddressById(Integer deliveryAddressId);
    DeliveryAddressResponse createDeliveryAddress(DeliveryAddressRequest deliveryAddressRequest);
    Optional<DeliveryAddressResponse> updateDeliveryAddress(Integer deliveryAddressId, DeliveryAddressRequest deliveryAddressRequest);
    Optional<DeliveryAddressResponse> removerDeliveryAddress(Integer deliveryAddressId);
}
