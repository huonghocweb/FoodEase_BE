package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import poly.foodease.Model.Entity.ReservationOrderPaymentStatus;
import poly.foodease.Model.Response.ReservationOrderPaymentStatusResponse;

@Mapper(componentModel = "spring")
public class ReservationOrderPaymentStatusMapper {
    public ReservationOrderPaymentStatusResponse convertEnToRes(ReservationOrderPaymentStatus reservationPaymentStatus){
        return ReservationOrderPaymentStatusResponse.builder()
                .reservationPaymentStatusId(reservationPaymentStatus.getReservationPaymentStatusId())
                .reservationPaymentStatusName(reservationPaymentStatus.getReservationPaymentStatusName())
                .build();
    }
}
