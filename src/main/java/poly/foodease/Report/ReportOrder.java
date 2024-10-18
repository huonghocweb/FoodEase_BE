package poly.foodease.Report;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportOrder {
	@Id
	private LocalDate orderDate;
	private LocalTime orderTime;
	private Double totalPrice;
	private Long totalQuantity;
}
