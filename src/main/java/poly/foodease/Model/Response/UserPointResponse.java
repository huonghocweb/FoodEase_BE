package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPointResponse {
    private Double totalPoint;
    private Double availablePoint;
    private Double usedPoint;
    private UserResponse user;
}
