package poly.foodease.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Response.ReservationOrderResponse;

@Service
public interface ReservationOrderService {
    Page<ReservationOrderResponse> getAllReservationOrder(Pageable pageable);
    ReservationOrderResponse getReservationOrderById(Integer reservationOrderId);
    ReservationOrderResponse getReservationOrderByReservationId(Integer reservationId);
     ReservationOrderResponse changeTableInReservationOrder(Integer reservationOrderId, Integer resTableId);
}
