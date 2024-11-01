package poly.foodease.Mapper;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Model.Entity.Reservation;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Model.Response.ReservationResponse;
import poly.foodease.Repository.*;

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
    @Autowired
    private TableServicesMapper tableServicesMapper;
    @Autowired
    private FoodMapper foodMapper;
    @Autowired
    private FoodsDao foodsDao;

    public ReservationResponse convertEnToRes(Reservation reservation){
        return ReservationResponse.builder()
                .reservationId(reservation.getReservationId())
                .totalDeposit(reservation.getTotalDeposit())
                .guests(reservation.getGuests())
                .checkinTime(reservation.getCheckinTime())
                .checkoutTime(reservation.getCheckoutTime())
                .bookTime(reservation.getBookTime())
                .checkinCode(reservation.getCheckinCode())
                .user(userMapper.convertEnToRes(reservation.getUser()))
                .resTable(resTableMapper.convertEnToRes(reservation.getResTable()))
                .reservationStatus(reservationStatusMapper.convertEnToRes(reservation.getReservationStatus()))
                .services(reservation.getServices() != null ? reservation.getServices().stream()
                        .map(tableServicesMapper :: convertEnToRes)
                        .collect(Collectors.toList()) : null)
                .foods(reservation.getFoods() != null ? reservation.getFoods().stream()
                        .map(foodMapper :: converEntoResponse)
                        .collect(Collectors.toList()): null)
                .build();
    }

    public Reservation convertReqToEn(ReservationRequest reservationRequest){
        return Reservation.builder()
                .totalDeposit(reservationRequest.getTotalDeposit())
                .checkinTime(reservationRequest.getCheckinTime())
                .checkoutTime(reservationRequest.getCheckoutTime())
                .bookTime(LocalDateTime.now())
                .checkinCode(reservationRequest.getCheckinCode())
                .reservationStatus(reservationRequest.getReservationStatusId() != null ? reservationStatusRepo.findById(reservationRequest.getReservationStatusId())
                        .orElseThrow(() -> new EntityNotFoundException("Not found Reservation Status")) : null)
                .user(reservationRequest.getUserId() != null ? userRepo.findById(reservationRequest.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("Not found User")) : null)
                .resTable(reservationRequest.getUserId() != null ? resTableRepo.findById(reservationRequest.getResTableIds())
                        .orElseThrow(() -> new EntityNotFoundException("Not found ResTable")) : null)
                .services(reservationRequest.getServiceIds() != null ? reservationRequest.getServiceIds().stream()
                        .map(servicesId -> tableServiceRepo.findById(servicesId)
                                .orElseThrow(() -> new EntityNotFoundException("not found Services")))
                        .collect(Collectors.toList()): null)
                .foods(reservationRequest.getFoodIds() != null ? reservationRequest.getFoodIds().stream()
                        .map(foodId -> foodsDao.findById(foodId)
                                .orElseThrow(() -> new EntityNotFoundException("Not found Foods")))
                        .collect(Collectors.toList()) : null)
                .build();
    }

}
