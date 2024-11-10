package poly.foodease.Service;


import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StripeService {

    public static final String SUCCESS_URL= "/thanks/stripe";
    public static final String CANCEL_URL= "/thanks/stripe";
    private final RouterFunctionMapping routerFunctionMapping;

    @Value("${stripe.secret.key}")
    private String secretKey;

    public StripeService(RouterFunctionMapping routerFunctionMapping) {
        this.routerFunctionMapping = routerFunctionMapping;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

//    public PaymentIntent createPaymentIntent(String orderInfo,long totalPrice,String baseUrlReturn) throws StripeException {
//        String currency= "usd";
//        Map<String, Object> params = new HashMap<>();
//        params.put("totalPrice",totalPrice);
//        params.put("orderInfo",orderInfo);
//        params.put("baseUrlReturn",baseUrlReturn);
//        params.put("amount", totalPrice);
//        params.put("currency", currency);
//        params.put("payment_method_types", List.of("card"));
//        return PaymentIntent.create(params);
//    }

    public String createCheckoutSession(Integer orderInfo, Integer totalPrice, String baseReturnUrl , List<Map<String,Object>> cartItems) throws StripeException {
        System.out.println("Create Check Out Stripe");
        cartItems.forEach(cartItem -> {
            String idPrice = (String) cartItem.get("idPrice");
            Integer quantity = (Integer) cartItem.get("quantity");
            System.out.println("Id Price : " + idPrice);
            System.out.println("Quantity :" + quantity);
        });
        // Thiết lập các tham số tạo session
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTimeString = now.format(formatter);

//        SessionCreateParams params = SessionCreateParams.builder()
//                // Mode PAYMENT là thanh toán lập tức
//                .setMode(SessionCreateParams.Mode.PAYMENT)
//                .setSuccessUrl(baseReturnUrl + SUCCESS_URL + "?session_id={CHECKOUT_SESSION_ID}")
//                .setCancelUrl(baseReturnUrl + CANCEL_URL + "?session_id={CHECKOUT_SESSION_ID}")
//                .addLineItem(
//                        SessionCreateParams.LineItem.builder()
//                                .setPrice("price_1PvAsPDsB54lSno8bN7sgmA6")
//                                .setQuantity(1L)
//                                .build())
//                .putMetadata("orderInfo", orderInfo)
//                .putMetadata("totalPrice", String.valueOf(totalPrice))
//                .putMetadata("dateTime", dateTimeString)
//                .build();
//        Session session = Session.create(params);
//        return session.getUrl();

        // SessionCreateParams là phương thức để cấu hình tham số khi thanh toán với Stripe
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder();
        paramsBuilder.setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(baseReturnUrl + SUCCESS_URL + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(baseReturnUrl + CANCEL_URL + "?session_id={CHECKOUT_SESSION_ID}");
        cartItems.forEach(cartItem -> {
            String idPrice = (String) cartItem.get("idPrice");
            Integer quantity = (Integer) cartItem.get("quantity");
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setPrice(idPrice)
                            .setQuantity(quantity.longValue())
                            .build()
            );
        });
        paramsBuilder.putMetadata("orderInfo", String.valueOf(orderInfo))
                .putMetadata("totalPrice", String.valueOf(totalPrice))
                .putMetadata("dateTime", dateTimeString);
        SessionCreateParams params = paramsBuilder.build();
        Session session = Session.create(params);
        return session.getUrl();
    }

    public Session returnPayment(String session_id){
        try {
            return Session.retrieve(session_id);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}

