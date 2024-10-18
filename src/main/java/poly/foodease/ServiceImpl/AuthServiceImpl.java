package poly.foodease.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Response.AuthResponse;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Service.AuthService;
import poly.foodease.Service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserService userService;

    @Override
    public AuthResponse createAuthResponse(UserDetails userDetails, String jwtToken){
        AuthResponse authRes= new AuthResponse();
        List<String > roles= userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        UserResponse userResponse= userService.getUserByUsername(userDetails.getUsername()).get();
        authRes.setUserId(userResponse.getUserId());
        authRes.setRoles(roles);
        authRes.setUsername(userDetails.getUsername());
        authRes.setJwtToken(jwtToken);
        return authRes;
    }
}