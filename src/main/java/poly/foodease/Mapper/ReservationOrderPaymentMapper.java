package poly.foodease.Mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import poly.foodease.Model.Entity.ReservationOrderPayment;
import poly.foodease.Model.Request.ReservationOrderPaymentRequest;
import poly.foodease.Model.Response.ReservationOrderPaymentResponse;
import poly.foodease.Repository.PaymentMethodRepo;
import poly.foodease.Repository.ReservationOrderPaymentStatusRepo;
import poly.foodease.Repository.ReservationOrderRepo;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public abstract class ReservationOrderPaymentMapper {

    @Autowired
    private ReservationOrderRepo reservationOrderRepo;
    @Autowired
    private ReservationOrderMapper reservationOrderMapper;
    @Autowired
    private PaymentMethodMapper paymentMethodMapper;
    @Autowired
    private PaymentMethodRepo paymentMethodRepo;
    @Autowired
    private ReservationOrderPaymentStatusMapper reservationPaymentStatusMapper;
    @Autowired
    private ReservationOrderPaymentStatusRepo reservationOrderPaymentStatusRepo;

    public ReservationOrderPaymentResponse convertEnToRes(ReservationOrderPayment reservationOrderPayment){
        return ReservationOrderPaymentResponse.builder()
                .reservationOrderPaymentId(reservationOrderPayment.getReservationOrderPaymentId())
                .paymentMethod(reservationOrderPayment.getPaymentMethod() != null ?
                        paymentMethodMapper.convertEnToRes(reservationOrderPayment.getPaymentMethod()) : null)
                .paymentDateTime(reservationOrderPayment.getPaymentDateTime())
                .totalAmount(reservationOrderPayment.getTotalAmount())
                .reservationOrderPaymentStatus(reservationOrderPayment.getReservationPaymentStatus() != null ?
                        reservationPaymentStatusMapper.convertEnToRes(reservationOrderPayment.getReservationPaymentStatus()) : null)
                .reservationOrder(reservationOrderPayment.getReservationOrder() != null ?
                        reservationOrderMapper.convertEnToRes(reservationOrderPayment.getReservationOrder()) : null)
                .build();
    }

    public ReservationOrderPayment convertReqToEn(ReservationOrderPaymentRequest reservationOrderPaymentRequest){
        return ReservationOrderPayment.builder()
                .paymentMethod(reservationOrderPaymentRequest.getPaymentMethodId() != null ?
                        paymentMethodRepo.findById(reservationOrderPaymentRequest.getPaymentMethodId())
                                .orElseThrow(() -> new EntityNotFoundException("Not found Payment Method")) : null)
                .paymentDateTime(reservationOrderPaymentRequest.getPaymentDateTime() != null ?
                        reservationOrderPaymentRequest.getPaymentDateTime()  : LocalDateTime.now())
                .totalAmount(reservationOrderPaymentRequest.getTotalAmount())
                .reservationOrder(reservationOrderPaymentRequest.getReservationOrderId() != null ?
                        reservationOrderRepo.findById(reservationOrderPaymentRequest.getReservationOrderId())
                                .orElseThrow(() -> new EntityNotFoundException("Not found ReservationOrder")) : null)
                .reservationPaymentStatus(reservationOrderPaymentRequest.getReservationOrderPaymentStatusId() != null ?
                        reservationOrderPaymentStatusRepo.findById(reservationOrderPaymentRequest.getReservationOrderPaymentStatusId())
                                .orElseThrow(() -> new EntityNotFoundException("not found ReservationOrderPayment Status")): null)
                .build();
    }
}
