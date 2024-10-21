package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableServicesResponse {
    private Integer serviceId;
    private String serviceName;
    private String imageUrl;
    private Double servicePrice;
    private String description;
}
