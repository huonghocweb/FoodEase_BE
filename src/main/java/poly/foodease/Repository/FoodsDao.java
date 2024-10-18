package poly.foodease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.foodease.Model.Entity.Foods;


public interface FoodsDao extends JpaRepository<Foods, Integer>{

	@Query("SELECT f FROM Foods f where f.category.categoryId = 1")
	List<Foods> findByCategoryMainDishes();
	@Query("SELECT f FROM Foods f where f.category.categoryId = 2")
	List<Foods> findByCategoryDrink();
}
