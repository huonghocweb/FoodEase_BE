package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import poly.foodease.Model.Entity.ReservationBillDetails;
import poly.foodease.Model.Response.ReservationBillDetailsResponse;

@Mapper(componentModel = "spring")
public abstract class ReservationBillDetailMapper {
    @Autowired
    private FoodMapper foodMapper;

    public ReservationBillDetailsResponse convertEnToRes(ReservationBillDetails reservationBillDetails){
        return ReservationBillDetailsResponse.builder()
                .reservationBillDetailId(reservationBillDetails.getReservationBillDetailId())
                .price(reservationBillDetails.getPrice())
                .quantity(reservationBillDetails.getQuantity())
                .foods(reservationBillDetails.getFoods() != null ? foodMapper.converEntoResponse(reservationBillDetails.getFoods()) : null)
                .build();
    }
}
