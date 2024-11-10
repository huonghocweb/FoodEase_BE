package poly.foodease.Model.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
// Hòa Tạo
@Data
@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String email; // Thêm trường email

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = true) // Cho phép null
    private User user;


    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // Constructor mặc định
    public PasswordResetToken() {}

    // Constructor mới với tất cả các tham số
    public PasswordResetToken(String token, User user, String email, LocalDateTime expiryDate) {
        this.token = token;
        this.user = user; // Có thể là null nếu không cần liên kết với User
        this.email = email; // Thêm email vào constructor
        this.expiryDate = expiryDate;
    }
}
