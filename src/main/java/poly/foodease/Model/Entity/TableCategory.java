package poly.foodease.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="table_categories")
public class TableCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="table_catetgory_id")
    private Integer tableCategoryId;

    @Column(name="table_category_name")
    private String tableCategoryName;

    @Column(name="price")
    private Double price;
}
