package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResTableResponse {
    private Integer tableId;
    private String tableName;
    private Integer capacity;
    private Boolean isAvailable; // true nếu bàn còn trống
}
