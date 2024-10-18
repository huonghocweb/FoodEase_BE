package poly.foodease.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Response.AuthResponse;

@Service
public interface AuthService {
    AuthResponse createAuthResponse(UserDetails userDetails, String jwtToken);
}