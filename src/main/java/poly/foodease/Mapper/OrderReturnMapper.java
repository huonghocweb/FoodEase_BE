package poly.foodease.Mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import poly.foodease.Model.Entity.OrderReturn;
import poly.foodease.Model.Request.OrderReturnRequest;
import poly.foodease.Model.Response.OrderReturnResponse;
import poly.foodease.Repository.FoodVariationsDao;
import poly.foodease.Repository.OrderRepo;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public abstract class OrderReturnMapper {
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    private FoodVariationsDao foodVariationsDao;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private FoodVariationMapper foodVariationMapper;

    public OrderReturnResponse convertEnToRes(OrderReturn orderReturn){
        return OrderReturnResponse.builder()
                .orderReturnId(orderReturn.getOrderReturnId())
                .reason(orderReturn.getReason())
                .returnDateTime(orderReturn.getReturnDateTime())
                .status(orderReturn.getStatus())
                .order(orderMapper.convertEnToRes(orderReturn.getOrder()))
                .foodVariation(foodVariationMapper.converEnToReponse(orderReturn.getFoodVariations()))
                .build();
    }

    public OrderReturn convertReqToEn(OrderReturnRequest orderReturnRequest){
        return OrderReturn.builder()
                .reason(orderReturnRequest.getReason())
                .status(orderReturnRequest.getStatus())
                .returnDateTime(LocalDateTime.now())
                .order(orderRepo.findById(orderReturnRequest.getOrderId())
                        .orElseThrow(() -> new EntityNotFoundException("Order Not found")))
                .foodVariations(foodVariationsDao.findById(orderReturnRequest.getFoodVaId())
                        .orElseThrow(()-> new EntityNotFoundException("ProductVa Not found")))
                .build();
    }

    public OrderReturnRequest convertResToReq(OrderReturnResponse orderReturnResponse){
        return OrderReturnRequest.builder()
                .status(orderReturnResponse.getStatus())
                .reason(orderReturnResponse.getReason())
                .orderId(orderReturnResponse.getOrder().getOrderId())
                .foodVaId(orderReturnResponse.getFoodVariation().getFoodVariationId())
                .build();
    }
}