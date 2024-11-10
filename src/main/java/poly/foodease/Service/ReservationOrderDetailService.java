package poly.foodease.Service;

import org.springframework.stereotype.Service;
import poly.foodease.Model.Response.ReservationOrderDetailResponse;

import java.util.List;
import java.util.Map;

@Service
public interface ReservationOrderDetailService {
     List<ReservationOrderDetailResponse> createReservationOrderDetail(Integer reservationId, Map<Integer , Integer> foodOrderItem);
}
