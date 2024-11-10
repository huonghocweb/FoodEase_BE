package poly.foodease.Mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import poly.foodease.Model.Entity.OrderDetails;
import poly.foodease.Model.Request.OrderDetailsRequest;
import poly.foodease.Model.Response.OrderDetailsResponse;
import poly.foodease.Repository.FoodVariationsDao;
import poly.foodease.Repository.OrderRepo;

@Mapper(componentModel = "spring")
public abstract class OrderDetailsMapper {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private FoodVariationsDao foodVariationsDao;


    public OrderDetailsResponse convertEnToRes(OrderDetails orderDetails){
        return OrderDetailsResponse.builder()
                .orderDetailsId(orderDetails.getOrderDetailsId())
                .price(orderDetails.getPrice())
                .quantity(orderDetails.getQuantity())
                .foodVariations(orderDetails.getFoodVariations())
                .build();
    }
    public OrderDetails convertReqToEn(OrderDetailsRequest orderDetailsRequest){
        return OrderDetails.builder()
                .price(orderDetailsRequest.getPrice())
                .quantity(orderDetailsRequest.getQuantity())
                .foodVariations(orderDetailsRequest.getFoodVaId() != null ? foodVariationsDao.findById(orderDetailsRequest.getFoodVaId())
                        .orElseThrow(()-> new EntityNotFoundException("not found FoodVariaiton")): null)
                .order(orderRepo.findById(orderDetailsRequest.getOrderId())
                        .orElseThrow(() -> new EntityNotFoundException("Not found Order")))
                .build();
    }
}
