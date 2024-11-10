package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import poly.foodease.Model.Entity.PaymentMethod;
import poly.foodease.Model.Response.PaymentMethodResponse;

@Mapper(componentModel = "spring")
public class PaymentMethodMapper {

    public PaymentMethodResponse convertEnToRes(PaymentMethod paymentMethod){
        return PaymentMethodResponse.builder()
                .paymentId(paymentMethod.getPaymentMethodId())
                .paymentName(paymentMethod.getPaymentName())
                .imageUrl(paymentMethod.getImageUrl())
                .build();
    }
}
