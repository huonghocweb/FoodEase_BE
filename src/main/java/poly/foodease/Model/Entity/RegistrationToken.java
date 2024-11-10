package poly.foodease.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registration_token")
public class RegistrationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public RegistrationToken(String token, String email, LocalDateTime expiryDate) {
        this.token = token;
        this.email = email;
        this.expiryDate = expiryDate;
    }

    // getters and setters
}
