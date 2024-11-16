package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResTableMapResponse {
    private Integer resTableId;
    private String resTableName;
    private Integer capacity;
    private Integer price;
    private String imageUrl;
    private Integer statusName;
    private String categoryName;
}
