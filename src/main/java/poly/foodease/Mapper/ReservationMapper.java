package poly.foodease.Mapper;

import org.springframework.stereotype.Component;

import poly.foodease.Model.Entity.ResTable;
import poly.foodease.Model.Entity.Reservation;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Model.Response.ReservationResponse;

@Component
public class ReservationMapper {

    public Reservation toEntity(ReservationRequest request) {
        Reservation reservation = new Reservation();
        reservation.setName(request.getName());
        reservation.setEmail(request.getEmail());
        reservation.setPhone(request.getPhone());
        reservation.setReservationDate(request.getReservationDate());
        reservation.setReservationTime(request.getReservationTime());
        reservation.setGuests(request.getGuests());
        reservation.setRestaurantTable(new ResTable(request.getTableId())); // Set bàn
        reservation.setStatus("Pending"); // Trạng thái mặc định
        return reservation;
    }

    public ReservationResponse toResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setReservationId(reservation.getReservationId());
        response.setStatus(reservation.getStatus());
        response.setName(reservation.getName());
        response.setEmail(reservation.getEmail());
        response.setPhone(reservation.getPhone());
        response.setReservationDate(reservation.getReservationDate());
        response.setReservationTime(reservation.getReservationTime());
        response.setGuests(reservation.getGuests());
        response.setTableId(reservation.getRestaurantTable().getTableId());
        response.setTableName(reservation.getRestaurantTable().getTableName());
        return response;
    }
}
