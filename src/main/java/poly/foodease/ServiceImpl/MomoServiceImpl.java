package poly.foodease.ServiceImpl;


import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Model.Response.OrderDetailsResponse;
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Model.Response.PaymentInfo;
import poly.foodease.Repository.OrderRepo;
import poly.foodease.Service.CartService;
import poly.foodease.Service.MomoService;
import poly.foodease.Service.OrderDetailsService;
import poly.foodease.Service.PaymentService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MomoServiceImpl {

    @Autowired
    MomoService momoService;
    @Autowired
    HttpSession session;

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    PaymentService paymentService;


    PaymentInfo paymentInfo;
    String orderPaymentId;


    public String createUrlPaymentMomo( Integer orderInfo,long totalPrice, String baseUrlReturn,String username){
        String urlPayment = momoService.createPaymentRequest(String.valueOf(orderInfo), totalPrice, baseUrlReturn,username);
        return urlPayment;
    }

    // Map<String,Object> requestBody : IPN là cách hoạt động đúng của MOMO để xác thực thanh toán , nhưng yêu cầu url công khai
    public PaymentInfo verifyPayment(Map<String,String> inpData) throws Exception {
        System.out.println(inpData);
        System.out.println("VERIFY ");
        // Đưa ipnData mà Momo trả vef thông qua inpURL public vào hàm verifyPayment để tiến hành xác thực chữ ký giao dịch
        Integer verifyStatus  = momoService.verifyPayment(inpData);
        // Lấy ra các tham số từ ipnData
        String orderInfo= inpData.get("orderInfo");
        String orderID= inpData.get("orderId");
//        System.out.println("ORDER ID11 : " + orderID);
//        System.out.println("ORDER INFO : " + orderInfo);
        String paymentDateTime_Parameter = inpData.get("responseTime");
        String transactionId_Parameter = inpData.get("transId");
        String totalPrice_Parameter = inpData.get("amount");
        Integer paymentStatus;

        // Xử lý thời gian từ timeStap của momo thàn localDateTime
        long responseTime = Long.parseLong(paymentDateTime_Parameter); // Đổi sang thời gian cần thiết
        Instant instant = Instant.ofEpochMilli(responseTime);
        LocalDateTime paymentDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        //System.out.println(paymentDateTime);

        // Lấy ra username từ extraData
        String username = inpData.get("extraData");

        // Tách couponId và OrderInfo bởi orderInfo được gộp lại trước đó.
//        String couponId = "null";
//        String orderInfo= "null";
//        if (orderInfo_Parameter != null && orderInfo_Parameter.contains("|couponId:")) {
//            // Tách chuỗi ban đầu theo "|couponId:"
//            String[] parts = orderInfo_Parameter.split("\\|couponId:");
//            // Phần đầu là orderInfo
//            orderInfo = parts[0].trim();
//            // Phần thứ hai là couponId
//            if (parts.length > 1) {
//                couponId= parts[1].trim();
//            }
//        }

        // Nếu trả về 0 là xác thực giao dịch thành công với momo
        System.out.println("verifyStatus : " + verifyStatus);
        if(verifyStatus == 0){
            System.out.println("DA XAC THUC");
            // Trong request momo trả về có 1 trường result code là trạng thái thanh toán ==0 là thanh toán thành công
            if(inpData.get("resultCode").equals("0")){
                System.out.println("RESULT CODE : " + inpData.get("resultCode"));
                paymentStatus =1;
                System.out.println("ORDER ID : " +orderInfo);
                OrderResponse order = paymentService.updatePaymentSuccess(Integer.valueOf(orderInfo));
                System.out.println("ORDER " + order.getOrderId());
              //  System.out.println("ORDER@@ " + order);
                paymentService.updateQuantityStock(order.getOrderId());
                if (order.getCoupon() != null){
                    System.out.println("UPDATE COUPON");
                    paymentService.updateCouponStorageAndUsedCount(order);
                }
                paymentService.sendEmail(order);

            }
            else{
                paymentStatus =0;
            }
            paymentInfo = paymentService.createPaymentInfo(String.valueOf(orderInfo), paymentStatus, String.valueOf(paymentDateTime), totalPrice_Parameter, transactionId_Parameter);
            orderPaymentId = inpData.get("orderId");
            System.out.println(orderPaymentId);

            // Trả về 1 là trường hợp không xác thực được giao dịch
        }else if(verifyStatus == 1 ){

        }
        return paymentInfo;
    }

    public PaymentInfo returnPayment(HttpServletRequest request){
        String orderId_request = request.getParameter("orderId");
        String orderID = request.getParameter("orderInfo");
        System.out.println("orderId_request : " + orderID);
        Order order = orderRepo.findById(Integer.valueOf(orderID))
                        .orElseThrow(() -> new EntityNotFoundException("NOT FOUND ORDER"));
        System.out.println("USER ID" + order.getUser().getUserId());
        System.out.println("CART BY ID :  " + cartService.getCart( order.getUser().getUserId()).getCartId());
       cartService.removeCart(1);
        System.out.println("orderPaymentId : " + orderPaymentId);
        System.out.println("PaymentInfo" + paymentInfo);
        if(Objects.equals(orderPaymentId, orderId_request)){
            cartService.removeCart(1);
            return paymentInfo;
        }
        return null;
    }

}

