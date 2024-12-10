package poly.foodease.Controller.Api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Entity.ReservationOrderPayment;
import poly.foodease.Model.Response.ReservationOrderPaymentResponse;
import poly.foodease.Service.*;
import poly.foodease.ServiceImpl.PayPalServiceImpl;
import poly.foodease.ServiceImpl.VnPayServiceImpl;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/paymentMethod")
public class PaymentMethodApi {

    @Autowired
    private VnPayServiceImpl vnPayService;
    @Autowired
    private PayPalServiceImpl payPalService;
    @Autowired
    private MomoService momoService;
    @Autowired
    private ReservationOrderPaymentService reservationOrderPaymentService;
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/payment/{reservationOrderId}/{totalPrice}")
    public ResponseEntity<Object> paymentByMethodId(
            @PathVariable("reservationOrderId") String reservationOrderId,
            @PathVariable("totalPrice") Integer totalPrice,
            @RequestParam(value = "paymentMethodId", required = false) Integer paymentMethodId,
            @RequestParam(value = "baseUrlReturn", required = false) String baseUrlReturn,
            @RequestParam(value = "userName",required = false) String userName
    ){
        Map<String,Object> result = new HashMap<>();
        System.out.println("reservationOrderId" + reservationOrderId);
        System.out.println("totalPrice" + totalPrice);
        System.out.println("paymentMethodId" + paymentMethodId);
        System.out.println("baseUrlReturn" + baseUrlReturn);
        System.out.println("userName" + userName);
        try {
            result.put("success",true);
            result.put("message","Payment By MethodId");
            if (paymentMethodId ==1 ){
                System.out.println("Vn Pay " + vnPayService.createPaymentUrl(totalPrice, reservationOrderId, baseUrlReturn));
                result.put("data", vnPayService.createPaymentUrl(totalPrice, reservationOrderId, baseUrlReturn));
            }else if (paymentMethodId ==2){
                System.out.println("Check out By PayPal");
                result.put("data",payPalService.createPaymenResertUrl(totalPrice, Integer.valueOf(reservationOrderId), baseUrlReturn, baseUrlReturn));
            }else if(paymentMethodId ==3 ){
                result.put("data",momoService.createPaymentRequest(String.valueOf(Integer.valueOf(reservationOrderId)), totalPrice, baseUrlReturn, userName));
            }else if(paymentMethodId == 5){
                System.out.println("Cash");
                ReservationOrderPaymentResponse reservationOrderPaymentResponse= reservationOrderPaymentService.createReservationOrderPayment(Integer.valueOf(reservationOrderId), paymentMethodId, Double.valueOf(totalPrice));
                if (reservationOrderPaymentResponse != null){
                    result.put("data", reservationService.checkoutReservation(reservationOrderPaymentResponse.getReservationOrder().getReservation().getReservationId()));
                }else {
                    result.put("data", null);
                }
            }
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data" , null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byPaypal/getPaymentInfoReser")
    public ResponseEntity<Object> getPaymentInfoReserByPaypal(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        System.out.println("Get Payment Reser Info PayPal");
        try {
            result.put("success",true);
            result.put("message","Get Payment Reser Info By PayPal");
            result.put("data",payPalService.returnPaymentReser(request));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }
}
