package poly.foodease.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="delivery_address")
public class DeliveryAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="delivery_address_id")
    private Integer deliveryAddressId;

    @Column(name="delivery_address_name")
    private String deliveryAddressName;

    @Column(name="province_id")
    private Integer provinceId;

    @Column(name="district_id")
    private Integer districtId;

    @Column(name="ward_code")
    private String wardCode;

    @Column(name="house_number")
    private String houseNumber;

    @Column(name="phone_address")
    private String phoneAddress;

    @Column(name="full_address")
    private String fullAddress;

    @Column(name="status")
    private Boolean status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
