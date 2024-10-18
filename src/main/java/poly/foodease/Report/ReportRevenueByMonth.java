package poly.foodease.Report;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportRevenueByMonth {
	@Id
	private int year;
	    private int month;
	    private Double totalPrice;
		private Long totalQuantity;
}
