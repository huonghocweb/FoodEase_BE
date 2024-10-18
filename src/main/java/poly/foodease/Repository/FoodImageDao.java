package poly.foodease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.foodease.Model.Entity.FoodImage;

public interface FoodImageDao extends JpaRepository<FoodImage, Integer>{

	@Query("SELECT fi FROM FoodImage fi where fi.foodId= ?1")
	List<FoodImage> findFoodImageByFoodId(Integer id);

	void deleteByFoodId(int foodId);
	
}
