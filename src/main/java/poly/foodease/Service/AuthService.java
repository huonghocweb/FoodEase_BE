package poly.foodease.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Response.AuthResponse;

import java.util.List;

@Service
public interface AuthService {
     AuthResponse createAuthResponseOAuth2(String userName, List<String> roles, String jwtToken);
    AuthResponse createAuthResponse(UserDetails userDetails, String jwtToken);
}