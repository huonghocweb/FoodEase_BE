package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.ReservationMapper;
import poly.foodease.Model.Entity.Reservation;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Model.Response.ReservationResponse;
import poly.foodease.Repository.ReservationRepo;
import poly.foodease.Service.ReservationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private ReservationRepo reservationRepo;

    @Override
    public Page<ReservationResponse> getAllReservation(Integer pageCurrent, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort = Sort.by(new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Pageable pageable = PageRequest.of(pageCurrent, pageSize, sort);
        Page<Reservation> reservationPage = reservationRepo.findAll(pageable);
        List<ReservationResponse> reservations = reservationPage.getContent().stream()
                .map(reservationMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(reservations,pageable, reservationPage.getTotalElements());
    }

    @Override
    public Optional<ReservationResponse> getReservationByReservationId(Integer reservationId) {
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Not found Reservation"));
        return Optional.of(reservationMapper.convertEnToRes(reservation));
    }

    @Override
    public Optional<ReservationResponse> getReservationByUserName(String userName) {
        Reservation reservation = reservationRepo.getReservationByReservationByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException("Not found Reservation"));
        return Optional.of(reservationMapper.convertEnToRes(reservation));
    }

    @Override
    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        Reservation reservation = reservationMapper.convertReqToEn(reservationRequest);
        Reservation reservationCreated = reservationRepo.save(reservation);
        return reservationMapper.convertEnToRes(reservationCreated);
    }

    @Override
    public Optional<ReservationResponse> updateReservation(Integer reservationId, ReservationRequest reservationRequest) {
        return Optional.of(reservationRepo.findById(reservationId).map(reservationExists -> {
            Reservation reservation = reservationMapper.convertReqToEn(reservationRequest);
            reservation.setReservationId(reservationExists.getReservationId());
            Reservation reservationUpdated = reservationRepo.save(reservation);
            return reservationMapper.convertEnToRes(reservationUpdated);
        })).orElseThrow(() -> new EntityNotFoundException("Not found Reservation"));
    }

    @Override
    public List<ReservationResponse> getReservedByTableIdAndDate(Integer tableId, LocalDate localDate) {
        LocalDateTime startOfDay = localDate.atTime(LocalTime.of(9, 0));
        LocalDateTime endOfDay = localDate.atTime(LocalTime.of(22, 0));
        List<Reservation> reservations = reservationRepo.getReservationsByTableIdAndDate(tableId, startOfDay, endOfDay);
        return reservations.stream()
                .map(reservationMapper :: convertEnToRes)
                .collect(Collectors.toList());

    }
}
