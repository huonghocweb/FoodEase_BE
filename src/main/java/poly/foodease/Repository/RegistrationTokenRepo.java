package poly.foodease.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.RegistrationToken;

import java.util.Optional;
public interface RegistrationTokenRepo extends JpaRepository<RegistrationToken, Integer> {
    Optional<RegistrationToken> findByToken(String token);
    Optional<RegistrationToken> findByEmail(String email);
    void deleteByEmail(String email);
}
