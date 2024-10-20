package poly.foodease.Mapper;

import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Model.Entity.Reservation;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Model.Response.ReservationResponse;
import poly.foodease.Repository.ResTableRepo;
import poly.foodease.Repository.ReservationStatusRepo;
import poly.foodease.Repository.TableServiceRepo;
import poly.foodease.Repository.UserRepo;

@Mapper(componentModel = "spring")
public abstract class ReservationMapper {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserMapper userMapper;
    @Autowired
     private ResTableMapper resTableMapper;
    @Autowired
    private ReservationStatusMapper reservationStatusMapper;
    @Autowired
    private ReservationStatusRepo reservationStatusRepo;
    @Autowired
    private ResTableRepo resTableRepo;
    @Autowired
    private TableServiceRepo tableServiceRepo;

    public ReservationResponse convertEnToRes(Reservation reservation){
        return ReservationResponse.builder()
                .reservationId(reservation.getReservationId())
                .totalDeposit(reservation.getTotalDeposit())
                .guests(reservation.getGuests())
                .reservationTime(reservation.getReservationTime())
                .reservationDate(reservation.getReservationDate())
                .user(userMapper.convertEnToRes(reservation.getUser()))
                .resTable(resTableMapper.convertEnToRes(reservation.getResTable()))
                .reservationStatus(reservationStatusMapper.convertEnToRes(reservation.getReservationStatus()))
                .build();
    }

    public Reservation convertReqToEn(ReservationRequest reservationRequest){
        return Reservation.builder()
                .totalDeposit(reservationRequest.getTotalDeposit())
                .reservationDate(reservationRequest.getReservationDate())
                .reservationTime(reservationRequest.getReservationTime())
                .reservationStatus(reservationStatusRepo.findById(reservationRequest.getReservationStatusId())
                        .orElseThrow(() -> new EntityNotFoundException("Not found Reservation Status")))
                .user(userRepo.findById(reservationRequest.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("Not found User")))
                .resTable(resTableRepo.findById(reservationRequest.getResTableIds())
                        .orElseThrow(() -> new EntityNotFoundException("Not found ResTable")))
                .services(reservationRequest.getServiceIds().stream()
                        .map(servicesId -> tableServiceRepo.findById(servicesId)
                                .orElseThrow(() -> new EntityNotFoundException("not found Services")))
                        .collect(Collectors.toList()))
                .build();
    }

}
