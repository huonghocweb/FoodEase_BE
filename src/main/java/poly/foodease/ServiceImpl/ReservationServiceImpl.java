package poly.foodease.ServiceImpl;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.foodease.Mapper.ReservationMapper;
import poly.foodease.Mapper.ReservationOrderPaymentMapper;
import poly.foodease.Model.Entity.MailInfo;
import poly.foodease.Model.Entity.Reservation;
import poly.foodease.Model.Entity.ReservationOrderPayment;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Model.Response.ReservationOrderPaymentResponse;
import poly.foodease.Model.Response.ReservationResponse;
import poly.foodease.Repository.*;
import poly.foodease.Service.MailService;
import poly.foodease.Service.ReservationService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
    @Autowired
    private MailService mailService;
    @Autowired
    private MailInfo mailInfo;
    @Autowired
    private ReservationOrderPaymentRepo reservationOrderPaymentRepo;
    @Autowired
    private ReservationOrderPaymentStatusRepo reservationOrderPaymentStatusRepo;
    @Autowired
    private ReservationOrderPaymentMapper reservationOrderPaymentMapper;


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
            reservation.setReservationStatus(reservationStatusRepo.findById(6)
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
        System.out.println(localDate);
        System.out.println("Start Date : " + startOfDay);
        System.out.println("End Date: " + endOfDay);
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
            reservation.setReservationStatus(reservationStatusRepo.findById(4)
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
    @Scheduled(fixedRate = 10000)
    @Transactional
    public List<ReservationResponse> changeReservationStatusToWaitingCheckin() {
        List<Reservation> reservations = reservationRepo.getReservationByReservationStatusId(List.of(1,2));
        reservations.forEach(reservation -> {
           // System.out.println(reservationMapper.convertEnToRes(reservation));
            LocalDateTime checkinTimeMinusOneHour = reservation.getCheckinTime().plusHours(1).truncatedTo(ChronoUnit.SECONDS);
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
//            System.out.println("checkinTimeMinusOneHour : " + checkinTimeMinusOneHour);
//            System.out.println("NOW : " + now);

            // Sử dụng Duration để tính toán độ chênh lệch giữa checkinTimeMinusOneHour và now
            Duration duration = Duration.between(checkinTimeMinusOneHour, now);

            // Kiểm tra xem độ chênh lệch có trong phạm vi 1 giây không
            if(reservation.getReservationStatus().getReservationStatusId() ==1){
                if (Math.abs(duration.getSeconds()) <= 1) {
                    //System.out.println("CHECK IN NOW");
                    mailInfo.setTo(reservation.getUser().getEmail());
                    StringBuilder bodyBuilder = new StringBuilder();
                    bodyBuilder.append("<html>");
                    bodyBuilder.append(" <p> Xin Chào Quý Khách , ").append(reservation.getUser().getFullName()).append("</p>");
                    bodyBuilder.append("<ul>");
                    bodyBuilder.append("<li> Bàn của bạn đã đến thời gian checkin : ").append("</li>");
                    bodyBuilder.append(" <li> Ngày Checkin : ").append(reservation.getCheckinTime().toLocalDate()).append("<br>")
                            .append(" Giờ Checkin : ").append(reservation.getCheckinTime().toLocalTime())
                            .append("</li>");
                    bodyBuilder.append("<li> Mã bàn ").append(reservation.getReservationId()).append("</li>");
                    bodyBuilder.append("<li> Tiền đã cọc ").append(reservation.getTotalDeposit()).append("</li>");
                    bodyBuilder.append("</ul>");
                    bodyBuilder.append("<p> Vui lòng có mặt và check in không quá 15 phút để được giữ lại bàn.").append("</p>");
                    bodyBuilder.append("<p>Quý khách có thể quét Mã QR để xem sản phẩm chi tiết.</p>");
                    bodyBuilder.append("<p>Chúc quý khách có một ngày vui vẻ!</p>");
                    bodyBuilder.append("<p>Trân trọng,<br>Công ty Victory Restaurant.</p>");
                    bodyBuilder.append("</html>");
                    mailInfo.setBody(bodyBuilder.toString());
                    mailInfo.setSubject("Notification to checkin your reservation");
                    try {
                        mailService.send(mailInfo);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                    reservation.setReservationStatus(reservationStatusRepo.findById(2)
                            .orElseThrow(() -> new EntityNotFoundException("Not found Reservation Status")));
                }
            }
            if(reservation.getReservationStatus().getReservationStatusId() ==2 ||reservation.getReservationStatus().getReservationStatusId() ==1 ) {
            //    System.out.println(reservationMapper.convertEnToRes(reservation));
                if (now.isEqual(reservation.getCheckinTime()) || now.isAfter(reservation.getCheckinTime())) {
                    System.out.println("The Table " + reservation.getResTable().getTableId() + " has reservationId is :" + reservation.getReservationId() + "waiting checkin");
                    reservation.setReservationStatus(reservationStatusRepo.findById(3)
                            .orElseThrow(() -> new EntityNotFoundException("Not found Reservation Status")));
                }else if(now.isAfter(reservation.getCheckoutTime()) || now.isAfter(reservation.getCheckinTime().plusMinutes(15))) {
                    System.out.println("The Table is cancel");
                    reservation.setReservationStatus(reservationStatusRepo.findById(8)
                            .orElseThrow(() -> new EntityNotFoundException("Not found Reservation Status")));
                }
            }

        });
        return reservationRepo.saveAll(reservations).stream()
                .map(reservationMapper::convertEnToRes)
                .collect(Collectors.toList());
    }



    @Override
    public ReservationOrderPaymentResponse checkoutReservation(Integer reservationId , Integer reservationOrderPaymentId) {
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(()-> new EntityNotFoundException("Not found Reservation"));
        reservation.setReservationStatus(reservationStatusRepo.findById(7)
                .orElseThrow(() -> new EntityNotFoundException("not found ReservationStatus")));
        Reservation reservationUpdated = reservationRepo.save(reservation);
        ReservationOrderPayment reservationOrderPayment = reservationOrderPaymentRepo.findById(reservationOrderPaymentId)
                .orElseThrow(()-> new EntityNotFoundException("not found Reservation Order Payment"));
        System.out.println("ReserOP : " + reservationOrderPayment.getReservationOrderPaymentId());
        reservationOrderPayment.setReservationPaymentStatus(reservationOrderPaymentStatusRepo.findById(3)
                .orElseThrow(() -> new EntityNotFoundException("not ofund Status")));
        System.out.println("ReserOP Status : " + reservationOrderPayment.getReservationPaymentStatus().getReservationPaymentStatusId());
        return reservationOrderPaymentMapper.convertEnToRes(reservationOrderPaymentRepo.save(reservationOrderPayment));
    }

//    @Override
//    public ReservationResponse orderFootToReservation(Integer reservationId, List<Integer> foodIds) {
//        Reservation reservation = reservationRepo.findById(reservationId)
//                .orElseThrow(() -> new EntityNotFoundException("Not found Reservation"));
//        reservation.setFoods(foodIds.stream().map( foodId -> {
//            return foodsDao.findById(foodId).orElseThrow(() -> new EntityNotFoundException("Not found Food"));
//        }).collect(Collectors.toList()));
//        Reservation reservationUpdated = reservationRepo.save(reservation);
//        return reservationMapper.convertEnToRes(reservationUpdated);
//    }
}
