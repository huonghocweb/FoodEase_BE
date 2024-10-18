package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResTableRequest {
    private String tableName;
    private Integer capacity;
    private Boolean isAvailable; // true nếu bàn còn trống
}
