package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TableServicesRequest {
    private String serviceName;
    private String imageUrl;
    private Double servicePrice;
    private String description;
}
