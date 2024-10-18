package poly.foodease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.foodease.Model.Entity.FoodReview;


public interface FoodReviewDao extends JpaRepository<FoodReview, Integer>{
@Query("SELECT fr FROM FoodReview fr where fr.foodId = ?1")
List<FoodReview> findFoodReviewByFoodId(Integer id);
}
