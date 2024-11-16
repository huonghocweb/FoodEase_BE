package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.ReservationOrderPaymentMapper;
import poly.foodease.Model.Entity.ReservationOrder;
import poly.foodease.Model.Entity.ReservationOrderPayment;
import poly.foodease.Model.Request.ReservationOrderPaymentRequest;
import poly.foodease.Model.Request.ReservationOrderRequest;
import poly.foodease.Model.Response.ReservationOrderPaymentResponse;
import poly.foodease.Repository.ReservationOrderPaymentRepo;
import poly.foodease.Repository.ReservationOrderRepo;
import poly.foodease.Service.ReservationOrderPaymentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationOrderPaymentServiceImpl implements ReservationOrderPaymentService {
    @Autowired
    private ReservationOrderPaymentRepo reservationOrderPaymentRepo;
    @Autowired
    private ReservationOrderPaymentMapper reservationOrderPaymentMapper;
    @Autowired
    private ReservationOrderRepo reservationOrderRepo;

    @Override
    public Page<ReservationOrderPaymentResponse> getAllReservationOrderPayment(Pageable pageable) {
        Page<ReservationOrderPayment> reservationOrderPaymentPage = reservationOrderPaymentRepo.findAll(pageable);
        List<ReservationOrderPaymentResponse> orderPaymentResponses = reservationOrderPaymentPage.getContent().stream()
                .map(reservationOrderPaymentMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(orderPaymentResponses, pageable , reservationOrderPaymentPage.getTotalElements());
    }

    @Override
    public ReservationOrderPaymentResponse getById(Integer reservationOrderPaymentId) {
        ReservationOrderPayment reservationOrderPayment = reservationOrderPaymentRepo.findById(reservationOrderPaymentId)
                .orElseThrow(() -> new EntityNotFoundException("Not found ReservationOrder Payment"));
        return reservationOrderPaymentMapper.convertEnToRes(reservationOrderPayment);
    }

    @Override

    public ReservationOrderPaymentResponse createReservationOrderPayment( Integer reservationOrderId, Integer paymentMethodId, Double totalAmount) {
        ReservationOrder reservationOrder = reservationOrderRepo.findById(reservationOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Not found ReservationOrder"));
        ReservationOrderPaymentRequest reservationOrderPaymentRequest = new ReservationOrderPaymentRequest();
        reservationOrderPaymentRequest.setReservationOrderId(reservationOrderId);
        reservationOrderPaymentRequest.setPaymentMethodId(paymentMethodId);
        reservationOrderPaymentRequest.setTotalAmount(totalAmount);
        ReservationOrderPayment reservationOrderPayment = reservationOrderPaymentMapper.convertReqToEn(reservationOrderPaymentRequest);
        ReservationOrderPayment reservationOrderPaymentCreated = reservationOrderPaymentRepo.save(reservationOrderPayment);
        return reservationOrderPaymentMapper.convertEnToRes(reservationOrderPaymentCreated);
    }

    @Override
    public ReservationOrderPaymentResponse getReservationOrderPaymentByReservationId(Integer reservationId) {
        ReservationOrderPayment reservationOrderPayment = reservationOrderPaymentRepo.getReservationOrderPaymentByReservationId(reservationId);
        return reservationOrderPaymentMapper.convertEnToRes(reservationOrderPayment);
    }
}
