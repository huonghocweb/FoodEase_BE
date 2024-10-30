package poly.foodease.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.Reservation;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Model.Response.ReservationResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface ReservationService {
    Page<ReservationResponse> getAllReservation(Pageable pageable);
    Optional<ReservationResponse> getReservationByReservationId(Integer reservationId);
    Optional<ReservationResponse> cancelRequestReservation(Integer reservationId);
    Page<ReservationResponse> getReservationByUserName(String userName , Integer pageCurrent,Integer pageSize, String sortOrder, String sortBy);
    ReservationResponse createReservation(ReservationRequest reservationRequest);
    Optional<ReservationResponse> updateReservation(Integer reservationId , ReservationRequest reservationRequest);
    List<ReservationResponse> getReservedByTableIdAndDate(Integer tableId , LocalDate localDate);
    ReservationResponse checkinReservation(Integer reservationId, Integer checkinKey);
    Page<ReservationResponse> getReservationByBookDate(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<ReservationResponse> getReservationByKeyWord(String keyWord, Pageable pageable);
}
