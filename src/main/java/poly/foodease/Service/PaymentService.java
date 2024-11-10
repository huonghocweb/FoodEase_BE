package poly.foodease.Service;

import com.google.zxing.WriterException;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Response.OrderDetailsResponse;
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Model.Response.PaymentInfo;
import poly.foodease.Model.Response.PaymentMethodResponse;

import java.io.IOException;
import java.util.List;

@Service
public interface PaymentService {
    PaymentInfo createPaymentInfo(String orderInfo, Integer paymentStatus, String totalPrice, String paymentDateTime, String transactionId);
    OrderResponse createOrder(Integer cartId, Integer couponId , Integer paymentMethodId, Integer shipMethodId , Integer leadTime, Integer shipFee, String deliveryAddress);
    OrderResponse updatePaymentSuccess(Integer orderId);
    List<OrderDetailsResponse> createOrderDetails(Integer orderId, Integer cartId);
    void updateCouponStorageAndUsedCount(String username,String couponId);
    List<OrderDetailsResponse> createOrderDetail(String OrderInfo, Integer orderId);
    void sendEmail (String username , OrderResponse orderResponse, List<OrderDetailsResponse> orderDetailResponses ) throws IOException, WriterException;
    List<PaymentMethodResponse> getAllPaymentMethod();
}
