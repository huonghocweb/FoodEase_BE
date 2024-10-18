package poly.foodease.Model.Entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name="user_name")
    private String userName;

    @Column(name="fullname")
    private String fullName;

    @Column(name="password")
    private String password;

    @Column(name="gender")
    private Boolean gender;

    @Column(name="address")
    private String address;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="birthday")
    private LocalDate birthday;

    @Column(name="email")
    private String email;

    @Column(name="status")
    private Boolean status;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "User_Role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<CouponStorage> couponStorages;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserPoint> userPoints;

    @OneToMany(mappedBy = "user")
    private List<WishList> WishLists; // Một User có nhiều Wish List
}
