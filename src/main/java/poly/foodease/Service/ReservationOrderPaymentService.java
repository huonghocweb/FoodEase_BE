package poly.foodease.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Request.ReservationOrderPaymentRequest;
import poly.foodease.Model.Response.ReservationOrderPaymentResponse;

@Service
public interface ReservationOrderPaymentService {
    Page<ReservationOrderPaymentResponse> getAllReservationOrderPayment(Pageable pageable);
    ReservationOrderPaymentResponse getById(Integer reservationOrderPaymentId);
    ReservationOrderPaymentResponse createReservationOrderPayment(Integer reservationOrderId, Integer paymentMethodId, Double totalAmount);
    ReservationOrderPaymentResponse getReservationOrderPaymentByReservationId(Integer reservationId);
}
