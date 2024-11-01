package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import poly.foodease.Model.Entity.ReservationBill;
import poly.foodease.Model.Request.ReservationBillRequest;
import poly.foodease.Model.Response.ReservationBillResponse;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ReservationBillMapper {

    @Autowired
    private ReservationBillDetailMapper reservationBillDetailMapper;

    public ReservationBillResponse convertEnToRes(ReservationBill reservationBill){
        return ReservationBillResponse.builder()
                .reservationBillId(reservationBill.getReservationBillId())
                .paymentDateTime(reservationBill.getPaymentDateTime())
                .totalPrice(reservationBill.getTotalPrice())
                .totalQuantity(reservationBill.getTotalQuantity())
                .status(reservationBill.getStatus())
                .reservationBillDetails(reservationBill.getReservationBillDetails() != null ? reservationBill.getReservationBillDetails().stream()
                        .map(reservationBillDetailMapper :: convertEnToRes)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public ReservationBill convertReqToEn(ReservationBillRequest reservationBillRequest){
        return ReservationBill.builder()
                .status(reservationBillRequest.getStatus())
                .paymentDateTime(reservationBillRequest.getPaymentDateTime())
                .totalQuantity(reservationBillRequest.getTotalQuantity())
                .totalPrice(reservationBillRequest.getTotalPrice())
                .build();
    }
}
