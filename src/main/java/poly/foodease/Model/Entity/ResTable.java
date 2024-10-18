package poly.foodease.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restables")
@Builder
public class ResTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id")
    private Integer tableId;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "is_available")
    private Boolean isAvailable;

    public ResTable(Integer tableId) {
        this.tableId = tableId;
    }
}

