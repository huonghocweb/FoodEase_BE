package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Request.PaymentMethodRevenueRequest;
import poly.foodease.Model.Response.PaymentMethodRevenueResponse;
import poly.foodease.Service.OrderService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/report")
public class ReportApi {
    @Autowired
    OrderService orderService;

    @PostMapping("/revenue-by-payment-method")
    public ResponseEntity<List<PaymentMethodRevenueResponse>> getRevenueByPaymentMethod(@RequestBody PaymentMethodRevenueRequest request) {
        List<PaymentMethodRevenueResponse> revenueReport = orderService.getRevenueByPaymentMethod(request);
        return ResponseEntity.ok(revenueReport);
    }
}
