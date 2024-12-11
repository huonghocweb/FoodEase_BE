package poly.foodease.ServiceImpl;


import com.google.zxing.WriterException;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.ReservationOrderPaymentMapper;
import poly.foodease.Model.Entity.Reservation;
import poly.foodease.Model.Entity.ReservationOrder;
import poly.foodease.Model.Entity.ReservationOrderPayment;
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Model.Response.PaymentInfo;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Repository.*;
import poly.foodease.Service.*;
import poly.foodease.Utils.JwtUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PayPalServiceImpl {
    @Autowired
    PayPalService paypalService;

    public static final String SUCCESS_URL = "/thanks/paypal";
    public static final String CANCEL_URL = "/thanks/paypal";

    public static final String SUCCESS_RESER_URL = "/thanks/reser/paypal";
    public static final String CANCEL_RESER_URL = "/thanks/reser/paypal";
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CartService cartService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private ReservationOrderRepo reservationOrderRepo;
    @Autowired
    private ReservationOrderPaymentRepo reservationOrderPaymentRepo;
    @Autowired
    private ReservationOrderPaymentStatusRepo reservationOrderPaymentStatusRepo;
    @Autowired
    private ReservationOrderPaymentMapper reservationOrderPaymentMapper;
    @Autowired
    private ReservationStatusRepo reservationStatusRepo;
    @Autowired
    private ReservationRepo reservationRepo;

    public String createPaymenResertUrl(Integer totalPrice,Integer orderInfo,String cancelUrl,String successUrl) throws PayPalRESTException {
        String urlPayment = "";
        Payment payment = paypalService.createPayment(
                Double.valueOf(totalPrice),orderInfo,
                cancelUrl +CANCEL_RESER_URL,successUrl +SUCCESS_RESER_URL  );
        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                urlPayment = link.getHref();
            }
        }
        return urlPayment;
    }

    public String createPaymentUrl(Integer totalPrice,Integer orderInfo,String cancelUrl,String successUrl) throws PayPalRESTException {
        String urlPayment = "";
        Payment payment = paypalService.createPayment(
                Double.valueOf(totalPrice),orderInfo,
                cancelUrl +CANCEL_URL,successUrl +SUCCESS_URL  );
        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                urlPayment = link.getHref();
            }
        }
        return urlPayment;
    }

    public Object returnPaymentReser(HttpServletRequest request) throws PayPalRESTException {
        System.out.println("1111" + request);
        Payment payment = paypalService.executePayment(request);
    //    System.out.println("Payment " + payment);
        System.out.println("saasas");
        String paymentId = payment.getId();
        String orderInfo_parameter = "";
        for (Transaction transaction : payment.getTransactions()) {
             orderInfo_parameter = transaction.getDescription();
        }
        System.out.println("123: " + orderInfo_parameter);
        ReservationOrderPayment reservationOrderPayment = reservationOrderPaymentRepo.findById(Integer.valueOf(orderInfo_parameter))
                .orElseThrow(() -> new EntityNotFoundException("not found ReservationOrderPayment"));
        Reservation reservation= reservationOrderPayment.getReservationOrder().getReservation();
        reservation.setReservationStatus(reservationStatusRepo.findById(7)
                .orElseThrow(() -> new EntityNotFoundException("not found Reservation Status") ));
        System.out.println("State Payment : " + payment.getState());
        if(payment.getState().equals("approved")){
            System.out.println("Payment Success");
            reservationOrderPayment.setReservationPaymentStatus(reservationOrderPaymentStatusRepo.findById(3)
                    .get());
            reservationRepo.save(reservation);
        }else {
            System.out.println("Payment Failed");
            reservationOrderPayment.setReservationPaymentStatus(reservationOrderPaymentStatusRepo.findById(2)
                    .get());
        }
        return reservationOrderPaymentMapper.convertEnToRes(reservationOrderPaymentRepo.save(reservationOrderPayment));
    }

    public PaymentInfo returnPayment(HttpServletRequest request) throws PayPalRESTException, IOException, WriterException {
        // Tạo ra đối tượng payment đại diện cho gia dịch
        System.out.println("REquuest" + request);
        Payment payment = paypalService.executePayment(request);
//        System.out.println("Payment : " + payment);
        // System.out.println(payment.toJSON());
        String paymentId = payment.getId();
        String datetime_parameter = payment.getCreateTime();
        String orderInfo_parameter = "";
        String totalPrice = "";
        String transactionId = "";
        Integer paymentStatus;
        // Parse dayTime từ request sang LocalDateTime
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(datetime_parameter, DateTimeFormatter.ISO_DATE_TIME).plusHours(7);
        // Lấy jwtToken từ request và lấy ra user;
        String jwtToken = request.getHeader("Authorization").substring(7);
        String username = jwtUtils.extractUsername(jwtToken);
        UserResponse userResponse = userService.getUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Not found User"));
        for (Transaction transaction : payment.getTransactions()) {
            transactionId = transaction.getRelatedResources().get(0).getSale().getId();
            totalPrice = transaction.getAmount().getTotal();
            orderInfo_parameter = transaction.getDescription();
        }
        // Lấy ra State của payment , nếu approved thì xử lý nghiệp vụ và hóa đơn
        OrderResponse orderResponse = null;
        if (payment.getState().equals("approved")) {
            paymentStatus = 1;
            System.out.println("Payment By Paypal Success");
            orderResponse = paymentService.updatePaymentSuccess(Integer.valueOf(orderInfo_parameter));
            // Xử lý nghiệp vụ CouponCout và CouponStorage
            paymentService.sendEmail(orderResponse);
            paymentService.updateQuantityStock(orderResponse.getOrderId());
            if (orderResponse.getCoupon() != null) {
                paymentService.updateCouponStorageAndUsedCount(orderResponse);
            }
        } else {
            System.out.println("Thất Bại");
            paymentStatus = 0;
        }
        cartService.removeCart(userResponse.getUserId());
        return paymentService.createPaymentInfo(orderInfo_parameter, paymentStatus, datetime_parameter, String.valueOf(orderResponse.getTotalPrice()), transactionId);
    }

}
