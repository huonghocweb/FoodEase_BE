package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {
    private Integer userId;
    private String username;
    private List<String> roles;
    private String jwtToken;
}