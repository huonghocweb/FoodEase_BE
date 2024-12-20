package poly.foodease.Model.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wish_list")
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_list_id")
    private Integer wishListId;

    @Column(name = "wish_list_name")
    private String wishListName;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "wishlist_foods", joinColumns = @JoinColumn(name = "wish_list_id"), inverseJoinColumns = @JoinColumn(name = "food_id"))
    private List<Foods> foods;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
