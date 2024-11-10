package poly.foodease.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.RegistrationToken;
import poly.foodease.Repository.RegistrationTokenRepo;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class RegistrationService {
//Hoa
    @Autowired
    private RegistrationTokenRepo registrationTokenRepo;

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 10000 + random.nextInt(90000); // 5-digit code
        return String.valueOf(code);
    }

    public RegistrationToken createRegistrationToken(String email) {
        String token = generateVerificationCode();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10); // Token expires in 10 minutes
        RegistrationToken registrationToken = new RegistrationToken(token, email, expiryDate);
        return registrationTokenRepo.save(registrationToken);
    }

    public Optional<RegistrationToken> validateToken(String token) {
        Optional<RegistrationToken> registrationToken = registrationTokenRepo.findByToken(token);

        if (registrationToken.isPresent() && registrationToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            return registrationToken;
        }
        return Optional.empty(); // Token invalid or expired
    }

    public void deleteToken(String email) {
        registrationTokenRepo.deleteByEmail(email);
    }
}
