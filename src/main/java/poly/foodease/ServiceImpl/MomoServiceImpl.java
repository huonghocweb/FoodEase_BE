package poly.foodease.ServiceImpl;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Response.OrderDetailsResponse;
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Model.Response.PaymentInfo;
import poly.foodease.Service.CartService;
import poly.foodease.Service.MomoService;
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
    PaymentService paymentService;

    PaymentInfo paymentInfo;
    String orderPaymentId;
    @Autowired
    private CartService cartService;

    public String createUrlPaymentMomo( Integer orderInfo,long totalPrice, String baseUrlReturn,String username){
        String urlPayment = momoService.createPaymentRequest( orderInfo, totalPrice, baseUrlReturn,username);
        return urlPayment;
    }

    // Map<String,Object> requestBody : IPN là cách hoạt động đúng của MOMO để xác thực thanh toán , nhưng yêu cầu url công khai
    public PaymentInfo verifyPayment(Map<String,String> inpData) throws Exception {
        System.out.println(inpData);
        // Đưa ipnData mà Momo trả vef thông qua inpURL public vào hàm verifyPayment để tiến hành xác thực chữ ký giao dịch
        Integer verifyStatus  = momoService.verifyPayment(inpData);
        // Lấy ra các tham số từ ipnData
        String orderInfo_Parameter = inpData.get("orderInfo");
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
        String couponId = "null";
        String orderInfo= "null";
        if (orderInfo_Parameter != null && orderInfo_Parameter.contains("|couponId:")) {
            // Tách chuỗi ban đầu theo "|couponId:"
            String[] parts = orderInfo_Parameter.split("\\|couponId:");
            // Phần đầu là orderInfo
            orderInfo = parts[0].trim();
            // Phần thứ hai là couponId
            if (parts.length > 1) {
                couponId= parts[1].trim();
            }
        }

        // Nếu trả về 0 là xác thực giao dịch thành công với momo
        System.out.println("verifyStatus : " + verifyStatus);
        if(verifyStatus == 0){
            // Trong request momo trả về có 1 trường result code là trạng thái thanh toán ==0 là thanh toán thành công
            if(inpData.get("resultCode").equals("0")){
                paymentStatus =1;
//                paymentService.sendEmail(username, orderResponse, orderDetailResponses);
//                paymentService.updateCouponStorageAndUsedCount(username, couponId);
                cartService.removeCart(Integer.valueOf(orderInfo));
            }
            else{
                paymentStatus =0;

            }
            paymentInfo = paymentService.createPaymentInfo(orderInfo, paymentStatus, String.valueOf(paymentDateTime), totalPrice_Parameter, transactionId_Parameter);
            orderPaymentId = inpData.get("orderId");
            System.out.println(orderPaymentId);
            // Trả về 1 là trường hợp không xác thực được giao dịch
        }else if(verifyStatus == 1 ){

        }
        return paymentInfo;
    }

    public PaymentInfo returnPayment(HttpServletRequest request){
        String orderId_request = request.getParameter("orderId");
        System.out.println("orderId_request : " + orderId_request);
        System.out.println("orderPaymentId : " + orderPaymentId);
        System.out.println("PaymentInfo" + paymentInfo);
        if(Objects.equals(orderPaymentId, orderId_request)){
            return paymentInfo;
        }
        return null;
    }

}

