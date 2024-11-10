package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.foodease.Model.Entity.FoodSize;

import java.util.List;


public interface FoodSizeDao extends JpaRepository<FoodSize, Integer>{
	@Query("SELECT fs FROM FoodSize fs where fs.foodSizeName Like ?1")
	FoodSize findFoodSizeBySize (String sizeName);
	@Query("SELECT fs FROM FoodSize fs INNER JOIN FoodVariations fv ON fs.foodSizeId = fv.foodSizeId WHERE fv.foodId = ?1")
	List<FoodSize> findSizeByFoodId(Integer id);
}
