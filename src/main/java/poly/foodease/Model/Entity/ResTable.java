package poly.foodease.Model.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="res_tables")
public class ResTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="table_id")
    private Integer tableId;

    @Column(name="capacity")
    private Integer capacity;

    @Column(name="is_available")
    private Boolean isAvailable;

    @Column(name="price")
    private Double price;

    @Column(name="deposit")
    private Double deposit;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="table_name")
    private String tableName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="table_catetgory_id")
    private TableCategory tableCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "resTable")
    private List<Reservation> reservations;


}
