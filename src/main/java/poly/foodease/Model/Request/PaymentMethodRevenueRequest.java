package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentMethodRevenueRequest {
    private Integer year; // Năm
    private Integer month; // Tháng
    private LocalDate startDate; // Ngày bắt đầu
    private LocalDate endDate; // Ngày kết thúc
}
