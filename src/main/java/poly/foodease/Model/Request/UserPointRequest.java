package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPointRequest {
    private Integer userId;
    private Double totalPoint;
    private Double availablePoint;
    private Double usedPoint;
    private LocalDateTime createAt;
}
