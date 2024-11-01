package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.foodease.Mapper.ReservationMapper;
import poly.foodease.Model.Entity.Reservation;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Model.Response.ReservationResponse;
import poly.foodease.Repository.ReservationRepo;
import poly.foodease.Repository.ReservationStatusRepo;
import poly.foodease.Service.ReservationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private ReservationRepo reservationRepo;
    @Autowired
    private ReservationStatusRepo reservationStatusRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Page<ReservationResponse> getAllReservation(Pageable pageable) {

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
    public Optional<ReservationResponse> cancelRequestReservation(Integer reservationId) {
        Reservation reservation =reservationRepo.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("not found Reservation"));
        if (LocalDateTime.now().plusHours(24).isBefore(reservation.getCheckinTime()) ){
            reservation.setReservationStatus(reservationStatusRepo.findById(4)
                    .orElseThrow(() -> new EntityNotFoundException("Not found Reservation Status")));
            System.out.println("Change to cancel Reservation Success");
            Reservation reservationUpdated = reservationRepo.save(reservation);
            return Optional.of(reservationMapper.convertEnToRes(reservationUpdated));
        }else {
            return Optional.empty();
        }

    }

    @Override
    public Page<ReservationResponse> getReservationByUserName(String userName, Integer pageCurrent,Integer pageSize, String sortOrder, String sortBy) {
        Sort sort = Sort.by(new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC,sortBy  ));
        Pageable pageable = PageRequest.of(pageCurrent, pageSize, sort);
        Page<Reservation> reservationPage = reservationRepo.getReservationByReservationByUserName(userName, pageable);
        List<ReservationResponse> reservations = reservationPage.getContent().stream()
                .map(reservationMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(reservations,pageable,reservationPage.getTotalElements());
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

    @Override
    public ReservationResponse checkinReservation(Integer reservationId, String checkinKey) {
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Not found Reservation"));
        String reservationCheckinCode = reservation.getCheckinCode();
        if(passwordEncoder.matches(checkinKey,reservationCheckinCode)){
            System.out.println("Checkin Thanh Cong");
            reservation.setReservationStatus(reservationStatusRepo.findById(3)
                    .orElseThrow(() -> new EntityNotFoundException("not Found Reservation Status")));
            Reservation reservationUpdated = reservationRepo.save(reservation);
            return reservationMapper.convertEnToRes(reservationUpdated);
        }else{
            return null;
        }

    }

    @Override
    public Page<ReservationResponse> getReservationByBookDate(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        Page<Reservation> reservationPage = reservationRepo.getReservationFilterByBookDate(pageable,  startDateTime, endDateTime);
        List<ReservationResponse> reservations = reservationPage.getContent().stream()
                .map(reservationMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(reservations,pageable, reservationPage.getTotalElements());
    }

    @Override
    public Page<ReservationResponse> getReservationByKeyWord(String keyWord, Pageable pageable) {
        Page<Reservation>reservationPage = reservationRepo.getReservationByKeyWord(keyWord, pageable);
        List<ReservationResponse> reservations = reservationPage.getContent().stream()
                .map(reservationMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>( reservations,pageable, reservationPage.getTotalElements());
    }

    @Override
    @Scheduled(fixedRate = 1000)
    @Transactional
    public List<ReservationResponse> changeReservationStatusToWaitingCheckin(){
        List<Reservation> reservations = reservationRepo.getReservationByReservationStatusId(1);
        reservations.forEach(reservation -> {
            if (reservation.getCheckinTime().isEqual(LocalDateTime.now())){
                reservation.setReservationStatus(reservationStatusRepo.findById(2)
                        .orElseThrow(() -> new EntityNotFoundException("Not found Reservation Status")));
            }
        });
        return reservationRepo.saveAll(reservations).stream()
                .map(reservationMapper :: convertEnToRes)
                .collect(Collectors.toList());
    }
}
