package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer userId;
    private String userName;
    private String fullName;
    private String password;
    private Boolean gender;
    private String address;
    private String phoneNumber;
    private String imageUrl;
    private LocalDate birthday;
    private String email;
    private Boolean status;
    private List<RoleResponse> roles ;
}