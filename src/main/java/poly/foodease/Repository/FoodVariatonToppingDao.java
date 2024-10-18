package poly.foodease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.foodease.Model.Entity.FoodVariationToppings;


public interface FoodVariatonToppingDao extends JpaRepository<FoodVariationToppings, Integer>{
@Query("SELECT fvt FROM FoodVariationToppings fvt WHERE fvt.foodVariationId = ?1")
List<FoodVariationToppings> findFoodVariationToppingById(Integer id);
}
