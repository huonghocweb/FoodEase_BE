package poly.foodease.ServiceImpl;


import com.google.zxing.WriterException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Response.OrderDetailsResponse;
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Model.Response.PaymentInfo;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Repository.OrderRepo;
import poly.foodease.Repository.UserRepo;
import poly.foodease.Service.*;
import poly.foodease.Utils.JwtUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VnPayServiceImpl {

    @Autowired
    private VnPayService vnPayService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepo orderRepo;


    // Tạo giao dịch bằng và trả về đường dẫn giúp dẫn tới trang thanh toán của VnPay
    public String createPaymentUrl(int totalPrice, String orderInfo, String returnUrl ) throws UnsupportedEncodingException {
        System.out.println("returnUrlr" + returnUrl);
        String paymentUrl= vnPayService.createOrder(totalPrice, orderInfo, returnUrl );
        return paymentUrl;
    }

    public PaymentInfo returnPayment(HttpServletRequest request) throws IOException, ParseException, WriterException {
        PaymentInfo  paymentInfo = new PaymentInfo();
        // request chứa thông tin sẽ được đưa vào hàm orderReturn để kiếm tra bảo mật và cập nhật trạng thái
        // Sau đó trả về trạng thái thanh toán và các thông tin liên quan .
        int paymentStatus = vnPayService.orderReturn(request);
        // tao dto paymentInfo de lay du lieu payment cho font-end ;
        request.setCharacterEncoding("UTF-8");
        String totalPrice =request.getParameter("vnp_Amount");
        String vnp_PayDate = request.getParameter("vnp_PayDate");
        String vnp_OrderInfo= request.getParameter("vnp_OrderInfo");
        String vnp_TransactionId = request.getParameter("vnp_TransactionId");
        // OrderInfo do nguoi dung tu dinh nghia, o day dinh nghia la cardId == key trong Map<CartId,Cart> MapStore
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
       // LocalDateTime dateTime = LocalDateTime.parse(vnp_PayDate, formatter);
        String jwtToken = request.getHeader("Authorization").substring(7);
        UserResponse user= userService.getUserByUsername(jwtUtils.extractUsername(jwtToken))
                .orElseThrow(() -> new EntityNotFoundException("not found User"));
        Integer orderId = Integer.valueOf(vnp_OrderInfo);
        if(paymentStatus ==1 ){
          //  System.out.println("Payment Success");
            // Xử lý Coupon usedCount và CouponStorage
//           paymentService.updateCouponStorageAndUsedCount(username,couponId);
            // Nếu thanh toán thành công chuyển trạng thái thành Shipping
            System.out.println("ORDER ID : " + orderId);
//            Order order = orderRepo.findById(orderId).get();
//            System.out.println("ORDER REPO " + order.getOrderDetails().size());
            OrderResponse orderResponse = paymentService.updatePaymentSuccess(orderId);
            // System.out.println("ORDER : " + orderResponse);
            paymentInfo = paymentService.createPaymentInfo(vnp_OrderInfo, paymentStatus, String.valueOf(LocalDateTime.now()), String.valueOf(orderResponse.getTotalPrice()), vnp_TransactionId);
          //  List<OrderDetailsResponse> orderDetailsResponses = orderDetailsService.getOrderDetailsByOrderId(orderId);
            System.out.println("ORDER DETAILS : " + orderResponse.getOrderDetails().size());
            paymentService.updateQuantityStock(orderResponse.getOrderId());
            if (orderResponse.getCoupon() != null ){
                paymentService.updateCouponStorageAndUsedCount(orderResponse);
            }
            paymentService.sendEmail(orderResponse );
            System.out.println("Payment Success");
        }else{
            System.out.println("Payment Failed");
            paymentInfo.setPaymentStatus(0);
        }
        cartService.removeCart(user.getUserId());
       // System.out.println(paymentInfo + "Payment Info Return");
        System.out.println("122");
        return paymentInfo;
    }

}
