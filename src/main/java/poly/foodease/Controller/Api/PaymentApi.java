package poly.foodease.Controller.Api;

import com.paypal.base.rest.PayPalRESTException;
import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Model.Response.OrderDetailsResponse;
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Repository.OrderRepo;
import poly.foodease.Service.PaymentService;
import poly.foodease.ServiceImpl.MomoServiceImpl;
import poly.foodease.ServiceImpl.PayPalServiceImpl;
import poly.foodease.ServiceImpl.StripeServiceImpl;
import poly.foodease.ServiceImpl.VnPayServiceImpl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/payment")
public class PaymentApi {

    @Autowired
    VnPayServiceImpl vnPayService;
    @Autowired
    PayPalServiceImpl payPalService;
    @Autowired
    StripeServiceImpl stripeService;
    @Autowired
    MomoServiceImpl momoService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    private OrderRepo orderRepo;


    @PostMapping("/byVnpay/{totalPrice}/{cartId}")
    public ResponseEntity<Object> paymentVnPay(
            @PathVariable("totalPrice") int totalPrice,
            @PathVariable("cartId") Integer cartId,
            @RequestParam(value = "couponId",required = false) Integer couponId,
            @RequestParam(value = "leadTime" ) Integer leadTime,
            @RequestParam(value = "shipFee") Integer shipFee,
            @RequestParam(value = "deliveryAddress" ) String deliveryAddress,
            @RequestParam("baseReturnUrl") String baseReturnUrl
    ){
        Map<String,Object> result = new HashMap<>();
        // Tạo hóa đơn với trạng thái processing
        OrderResponse orderResponse=paymentService.createOrder(cartId, couponId, 1, 1,leadTime,shipFee,deliveryAddress);
        List<OrderDetailsResponse> orderDetailsResponses = paymentService.createOrderDetails(orderResponse.getOrderId(), cartId);
        try {
            result.put("success",true);
            result.put("message","Create Url Payment With VnPay");
            result.put("data",vnPayService.createPaymentUrl(totalPrice, String.valueOf(orderResponse.getOrderId()), baseReturnUrl));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/byPaypal/{totalPrice}/{cartId}")
    public ResponseEntity<Object> paymentByPayPal(
            @PathVariable("totalPrice") Integer totalPrice,
            @PathVariable("cartId") Integer cartId,
            @RequestParam(value = "couponId",required = false) Integer couponId,
            @RequestParam("leadTime") Integer leadTime,
            @RequestParam("shipFee") Integer shipFee,
            @RequestParam("deliveryAddress") String deliveryAddress,
            @RequestParam("baseReturnUrl") String baseReturnUrl
    ){
        System.out.println("base Return " + baseReturnUrl
        );
        OrderResponse orderResponse= paymentService.createOrder(cartId, couponId, 2, 1, leadTime, shipFee, deliveryAddress);
        List<OrderDetailsResponse> orderDetailsResponses = paymentService.createOrderDetails(orderResponse.getOrderId(), cartId);
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Create Url Payment with PayPal");
            System.out.println("Paypal" + payPalService.createPaymentUrl(totalPrice, orderResponse.getOrderId(), baseReturnUrl, baseReturnUrl));
            result.put("data",payPalService.createPaymentUrl(totalPrice, orderResponse.getOrderId(), baseReturnUrl, baseReturnUrl));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/byStripe/{totalPrice}/{cartId}")
    public ResponseEntity<Object> createPaymentWithStripe(
            @PathVariable("totalPrice") Integer totalPrice,
            @PathVariable("cartId") Integer cartId,
            @RequestParam(value = "couponId",required = false) Integer couponId,
            @RequestParam("leadTime") Integer leadTime,
            @RequestParam("shipFee") Integer shipFee,
            @RequestParam("deliveryAddress") String deliveryAddress,
            @RequestParam("baseReturnUrl") String baseReturnUrl,
            @RequestBody List<Map<String,Object>> cartItems
    ) throws StripeException {
        System.out.println("Stripe " + cartItems);
        OrderResponse orderResponse= paymentService.createOrder(cartId, couponId, 4, 1, leadTime, shipFee, deliveryAddress);
        List<OrderDetailsResponse> orderDetailsResponses = paymentService.createOrderDetails(orderResponse.getOrderId(), cartId);
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Create Payment Url with Stripe");
            result.put("data",stripeService.createPaymentUrlByStripe(orderResponse.getOrderId(), totalPrice, baseReturnUrl, cartItems));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }


    @PostMapping("/byMomo/{totalPrice}/{cartId}/{username}")
    public ResponseEntity<Object> createPaymentByMomo(
            @PathVariable("totalPrice") long totalPrice,
            @PathVariable("cartId") Integer cartId,
            @PathVariable("username") String username,
            @RequestParam(value = "couponId",required = false) Integer couponId,
            @RequestParam("leadTime") Integer leadTime,
            @RequestParam("shipFee") Integer shipFee,
            @RequestParam("deliveryAddress") String deliveryAddress,
            @RequestParam("baseReturnUrl") String baseReturnUrl
    ){
        System.out.println("payment Momo " + baseReturnUrl );
        OrderResponse orderResponse= paymentService.createOrder(cartId, couponId, 3, 1, leadTime, shipFee, deliveryAddress);
        List<OrderDetailsResponse> orderDetailsResponses = paymentService.createOrderDetails(orderResponse.getOrderId(), cartId);
        Double totalPriceDouble = orderResponse.getTotalPrice(); // Giả sử đây là Double.
        if (totalPriceDouble == null) {
            throw new IllegalArgumentException("Total price is null");
        }
        // Chuyển Double sang Integer:
        int totalPriceInteger = Math.toIntExact(Math.round(totalPriceDouble));

        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Create Payment With Momo");
            result.put("data",momoService.createUrlPaymentMomo(orderResponse.getOrderId(),totalPrice,baseReturnUrl,username));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/ipn")
    public void checkPaymentByIpnMomo(@RequestBody Map<String, String> ipnData) throws Exception {
        System.out.println("IPN Data" + ipnData);
        momoService.verifyPayment(ipnData);
    }

    @GetMapping("/byMomo/getPaymentInfo")
    public ResponseEntity<Object> getPaymentInfoByMomo(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        System.out.println("Get Payment Info byMomo");
        try {
            result.put("success",true);
            result.put("message","Get Payment Info By Momo");
            result.put("data",momoService.returnPayment(request));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byVnpay/getPaymentInfo")
    public ResponseEntity<Object> getPaymentInfoByVnPay(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        System.out.println("Get Payment Info VnPay");
        try {
            result.put("success",true);
            result.put("message","Get Payment Info By Vnpay");
            result.put("data",vnPayService.returnPayment(request));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byPaypal/getPaymentInfo")
    public ResponseEntity<Object> getPaymentInfoByPaypal(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        System.out.println("Get Payment Info PayPal");
        try {
            result.put("success",true);
            result.put("message","Get Payment Info By PayPal");
            result.put("data",payPalService.returnPayment(request));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byStripe/getPaymentInfo")
    public ResponseEntity<Object> getPaymentInfoBySripe(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        System.out.println("get payment Info by Stripe");
        try {
            result.put("success",true);
            result.put("message","Get Payment Info By Stripe");
            result.put("data",stripeService.returnPaymentByStripe(request));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllPaymentMethod(){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get All Payment Method");
            result.put("data",paymentService.getAllPaymentMethod());
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/paymentContinue/{orderId}")
    public ResponseEntity<Object> paymentContinue(
            @PathVariable("orderId") Integer orderId,
            @RequestParam("baseUrlReturn") String baseUrlReturn
    ) throws UnsupportedEncodingException, PayPalRESTException, StripeException {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("NOt found Entity"));
        Integer paymentMethodId = order.getPaymentMethod().getPaymentMethodId();
        List< Map<String,Object>> cartItems = new ArrayList<>();
        String urlPaymentReturn = "";
        Map<String,Object> cartItem = new HashMap<>();
        order.getOrderDetails().forEach(orderDetails -> {
            cartItem.put(String.valueOf(orderDetails.getFoodVariations().getFoodVariationId()), orderDetails.getQuantity());
            cartItems.add(cartItem);
            System.out.println(cartItem);
        });
        System.out.println("CartItems : " + cartItems);
        int totalPriceInteger = Math.toIntExact(Math.round(order.getTotalPrice()));
        if (paymentMethodId ==1){
            urlPaymentReturn = vnPayService.createPaymentUrl(totalPriceInteger, String.valueOf(order.getOrderId()), baseUrlReturn);
        }else if (paymentMethodId ==2 ){
            urlPaymentReturn = payPalService.createPaymentUrl(totalPriceInteger, order.getOrderId(), baseUrlReturn, baseUrlReturn);
        }else if(paymentMethodId == 4){
            urlPaymentReturn = stripeService.createPaymentUrlByStripe(order.getOrderId(), totalPriceInteger, baseUrlReturn,cartItems );
        } else if(paymentMethodId ==3){
            urlPaymentReturn = momoService.createUrlPaymentMomo(order.getOrderId(), totalPriceInteger, baseUrlReturn, order.getUser().getUserName());
        }
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get All Payment Method");
            result.put("data",urlPaymentReturn);
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

}
