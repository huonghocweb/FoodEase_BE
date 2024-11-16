package poly.foodease.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.Foods;


public interface FoodsDao extends JpaRepository<Foods, Integer>{

	@Query("SELECT f FROM Foods f where f.category.categoryId = 1")
	List<Foods> findByCategoryMainDishes();
	@Query("SELECT f FROM Foods f where f.category.categoryId = 2")
	List<Foods> findByCategoryDrink();


	//Hưởng
	@Query("SELECT f FROM Foods f WHERE f.foodName LIKE :foodName")
	Page<Foods> findFoodsByFoodName(@Param("foodName") String foodName,
									Pageable pageable);
	//Hưởng
}
