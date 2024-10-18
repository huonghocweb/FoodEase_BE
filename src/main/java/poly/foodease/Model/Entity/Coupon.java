package poly.foodease.Model.Entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="coupon_id")
    private Integer couponId;

    @Column(name="code")
    private String code;

    @Column(name="description")
    private String description;

    @Column(name="discountpercent")
    private Double discountpercent;

    @Column(name="max_discount_amount")
    private Double maxDiscountAmount;

    @Column(name ="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @Column(name ="use_limit")
    private Integer useLimit;

    @Column(name="used_count")
    private Integer usedCount;

    @Column(name="image_url")
    private String imageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "coupon")
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "coupon")
    private List<CouponStorage> couponStorages;

}
