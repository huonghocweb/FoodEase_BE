package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipMethodResponse {
    private Integer shipId;
    private String shipName;
    private Integer shipDuration;
    private Integer shipFee;
    private String shipDescription;
    private Boolean shipStatus;


}
