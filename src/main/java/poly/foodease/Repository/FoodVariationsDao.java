package poly.foodease.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.foodease.Model.Entity.FoodVariations;

public interface FoodVariationsDao extends JpaRepository<FoodVariations, Integer> {
    // @Query("SELECT fv FROM FoodVariations fv where fv.food.categoryId = 1 and
    // fv.foodSize.foodSizeId= 1")
    // List<FoodVariations> findByCategoryMainDishes();
    @Query("SELECT fv FROM FoodVariations fv where fv.food.categoryId = 1 and fv.foodSize.foodSizeId= 1")
    Page<FoodVariations> findByCategoryMainDishes(Pageable page);

    @Query("SELECT fv FROM FoodVariations fv where fv.food.categoryId = 2 and fv.foodSize.foodSizeId= 1")
    List<FoodVariations> findByCategoryDrink();

    @Query("SELECT fv FROM FoodVariations fv where fv.foodId = ?1 and fv.foodSize.foodSizeName like ?2")
    FoodVariations findFoodVariationBySize(Integer id, String sizeName);

    @Query("SELECT fv FROM FoodVariations fv where fv.foodSizeId=1 and  fv.food.foodName Like %?1%")
    List<FoodVariations> findFoodVariationByFoodName(String FoodName);

    @Query("SELECT fv FROM FoodVariations fv where fv.food.categoryId = ?1 and fv.foodSizeId=1")
    List<FoodVariations> findFoodVariationByCategoryId(Integer id);

    @Query("SELECT ord.foodVariations FROM OrderDetails ord where ord.order.user.userId = ?1")
    List<FoodVariations> findFoodVariationByUserId(Integer id);

    // chánh
    Optional<FoodVariations> findByFoodId(Integer foodId);
}
