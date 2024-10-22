package poly.foodease.Service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Model.Response.ReservationResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface ReservationService {
    Page<ReservationResponse> getAllReservation(Integer pageCurrent , Integer pageSize, String sortOrder, String sortBy);
    Optional<ReservationResponse> getReservationByReservationId(Integer reservationId);
    Optional<ReservationResponse> getReservationByUserName(String userName);
    ReservationResponse createReservation(ReservationRequest reservationRequest);
    Optional<ReservationResponse> updateReservation(Integer reservationId , ReservationRequest reservationRequest);
    List<ReservationResponse> getReservedByTableIdAndDate(Integer tableId , LocalDate localDate);
}
