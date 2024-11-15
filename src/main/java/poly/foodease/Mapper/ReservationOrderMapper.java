package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import poly.foodease.Model.Entity.ReservationOrder;
import poly.foodease.Model.Request.ReservationOrderRequest;
import poly.foodease.Model.Response.ReservationOrderResponse;

import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public class ReservationOrderMapper {
    @Autowired
    private ReservationOrderDetailMapper reservationOrderDetailMapper;
    @Autowired
    private ReservationMapper reservationMapper;

    public ReservationOrderResponse convertEnToRes(ReservationOrder reservationOrder){
        return ReservationOrderResponse.builder()
                .reservationOrderId(reservationOrder.getReservationOrderId())
                .orderDateTime(reservationOrder.getOrderDateTime())
                .totalPrice(reservationOrder.getTotalPrice())
                .totalServicePrice(reservationOrder.getTotalServicePrice())
                .totalQuantity(reservationOrder.getTotalQuantity())
                .status(reservationOrder.getStatus())
                .reservation(reservationOrder.getReservation() != null ?
                                reservationMapper.convertEnToRes(reservationOrder.getReservation()) : null)
                .reservationOrderDetails(reservationOrder.getReservationOrderDetails() != null ? reservationOrder.getReservationOrderDetails().stream()
                        .map(reservationOrderDetailMapper :: convertEnToRes)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public ReservationOrder convertReqToEn(ReservationOrderRequest reservationOrderRequest){
        return ReservationOrder.builder()
                .status(reservationOrderRequest.getStatus())
                .orderDateTime(reservationOrderRequest.getOrderDatTime())
                .totalQuantity(reservationOrderRequest.getTotalQuantity())
                .totalPrice(reservationOrderRequest.getTotalPrice())
                .totalServicePrice(reservationOrderRequest.getTotalServicePrice())
                .build();
    }
}
