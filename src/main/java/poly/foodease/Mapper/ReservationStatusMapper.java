package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import poly.foodease.Model.Entity.Reservation;
import poly.foodease.Model.Entity.ReservationStatus;
import poly.foodease.Model.Response.ReservationResponse;
import poly.foodease.Model.Response.ReservationStatusResponse;

@Mapper(componentModel = "spring")
public class ReservationStatusMapper {
    public ReservationStatusResponse convertEnToRes(ReservationStatus reservationStatus){
        return ReservationStatusResponse.builder()
                .reservationStatusId(reservationStatus.getReservationStatusId())
                .reservationStatusName(reservationStatus.getReservationStatusName())
                .build();
    }
}
