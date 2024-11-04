package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.foodease.Model.Entity.PasswordResetToken;
import poly.foodease.Model.Entity.User;
import java.util.Optional;
//Ho Tạo
@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
    Optional<PasswordResetToken> findByTokenAndEmail(String token, String email); // Thêm phương thức mới
}
