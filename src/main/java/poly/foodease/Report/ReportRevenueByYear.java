package poly.foodease.Report;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportRevenueByYear {
@Id
private Integer year;
private Double totalPrice;
private Long totalQuantity;

}
