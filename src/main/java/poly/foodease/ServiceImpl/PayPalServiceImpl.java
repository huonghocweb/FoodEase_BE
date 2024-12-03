package poly.foodease.ServiceImpl;


import com.google.zxing.WriterException;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Response.OrderDetailsResponse;
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Model.Response.PaymentInfo;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Service.*;
import poly.foodease.Utils.JwtUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PayPalServiceImpl {
    @Autowired
    PayPalService paypalService;

    public static final String SUCCESS_URL = "/thanks/paypal";
    public static final String CANCEL_URL = "/thanks/paypal";
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CartService cartService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private UserService userService;

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

    public PaymentInfo returnPayment(HttpServletRequest request) throws PayPalRESTException, IOException, WriterException {
        // Tạo ra đối tượng payment đại diện cho gia dịch
        Payment payment = paypalService.executePayment(request);
        // System.out.println(payment.toJSON());
        String paymentId =payment.getId();
        String datetime_parameter = payment.getCreateTime();
        String orderInfo_parameter = "";
        String totalPrice = "";
        String transactionId = "";
        Integer paymentStatus ;
        // Parse dayTime từ request sang LocalDateTime
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(datetime_parameter, DateTimeFormatter.ISO_DATE_TIME).plusHours(7);
        // Lấy jwtToken từ request và lấy ra user;
        String jwtToken = request.getHeader("Authorization").substring(7);
        String username = jwtUtils.extractUsername(jwtToken);
        UserResponse userResponse = userService.getUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Not found User"));
        for (Transaction transaction : payment.getTransactions()){
            transactionId = transaction.getRelatedResources().get(0).getSale().getId();
            totalPrice = transaction.getAmount().getTotal();
            orderInfo_parameter = transaction.getDescription();
        }
        // Lấy ra State của payment , nếu approved thì xử lý nghiệp vụ và hóa đơn
        if(payment.getState().equals("approved")){
            paymentStatus= 1;
            System.out.println("Payment By Paypal Success");
            OrderResponse orderResponse = paymentService.updatePaymentSuccess(Integer.valueOf(orderInfo_parameter));
            // Xử lý nghiệp vụ CouponCout và CouponStorage
            paymentService.sendEmail( orderResponse);
            paymentService.updateQuantityStock(orderResponse.getOrderId());
            if (orderResponse.getCoupon() != null){
                paymentService.updateCouponStorageAndUsedCount(orderResponse);
            }
        }else{
            System.out.println("Thất Bại");
            paymentStatus = 0;
        }
        cartService.removeCart(userResponse.getUserId());
        return paymentService.createPaymentInfo(orderInfo_parameter,paymentStatus,datetime_parameter,totalPrice,transactionId);
    }

}
