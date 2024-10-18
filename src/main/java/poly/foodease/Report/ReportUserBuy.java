package poly.foodease.Report;

import java.time.LocalDate;
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
public class ReportUserBuy {
@Id
private Integer userId;
private String fullName;
private Boolean gender;
private String phoneNumber;
private String address;
private LocalDate birthday;
private String email;
private Long totalQuantity;
private Double totalPrice;
}
