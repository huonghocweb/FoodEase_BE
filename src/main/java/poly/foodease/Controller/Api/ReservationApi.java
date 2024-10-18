package poly.foodease.Controller.Api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import poly.foodease.Mapper.ReservationMapper;
import poly.foodease.Model.Entity.Reservation;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Model.Response.ReservationResponse;
import poly.foodease.Service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = { RequestMethod.PATCH,
        RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.DELETE }, allowCredentials = "true")

public class ReservationApi {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationMapper reservationMapper;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationMapper.toEntity(reservationRequest);
        Reservation savedReservation = reservationService.createReservation(reservation);
        ReservationResponse response = reservationMapper.toResponse(savedReservation);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> responses = reservationService.getAllReservations().stream()
                .map(reservationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReservationResponse> updateReservationStatus(@PathVariable Integer id,
            @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        Reservation updatedReservation = reservationService.updateReservationStatus(id, status);
        ReservationResponse response = reservationMapper.toResponse(updatedReservation);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<String> acceptReservation(@PathVariable Integer id) {
        try {
            Reservation reservation = reservationService.getReservationById(id);
            reservationService.acceptReservation(reservation);
            return ResponseEntity.ok("Reservation accepted and email sent.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error accepting reservation.");
        }
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Integer id) {
        try {
            Reservation reservation = reservationService.getReservationById(id);
            reservationService.cancelReservation(reservation);
            return ResponseEntity.ok("Reservation cancelled and email sent.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error cancelling reservation.");
        }
    }
}
