package poly.foodease.Controller.Api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Service.InvoiceService;
import poly.foodease.Service.OrderService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/invoice")
@CrossOrigin("*")
public class InvoiceApi {
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<InputStreamResource> getInvoice(@PathVariable Integer orderId) throws IOException {
        // Lấy thông tin đơn hàng dựa vào orderId
        Order order = orderService.getOrderById(orderId);

        // Tạo hóa đơn PDF
        ByteArrayInputStream bis = invoiceService.generateInvoicePdf(order);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice_" + orderId + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
