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
    private Integer capacity;
    private Boolean isAvailable;
    private Double price;
    private Double deposit;
    private String imageUrl;
    private String tableName;
    private TableCategoryResponse tableCategory;

}
