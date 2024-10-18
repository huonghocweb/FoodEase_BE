package poly.foodease.Mapper;

import lombok.Data;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import poly.foodease.Model.Entity.OrderStatus;
import poly.foodease.Model.Response.OrderStatusResponse;

@Mapper(componentModel = "spring")
public abstract class OrderStatusMapper {

    public OrderStatusResponse convertEnToRes(OrderStatus orderStatus){
        return OrderStatusResponse.builder()
                .orderStatusId(orderStatus.getOrderStatusId())
                .orderStatusName(orderStatus.getOrderStatusName())
                .build();
    }
}
