package poly.foodease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import poly.foodease.Model.Entity.FoodReview;


public interface FoodReviewDao extends JpaRepository<FoodReview, Integer>{
@Query("SELECT fr FROM FoodReview fr where fr.foodId = ?1")
List<FoodReview> findFoodReviewByFoodId(Integer id);
@Query("SELECT o FROM FoodReview o  WHERE (:rating IS NULL OR o.rating = :rating) AND (FUNCTION('MONTH', o.reviewDate) = :month) AND (FUNCTION('YEAR', o.reviewDate) = :year)")
List<FoodReview> findByFilter(@Param("rating") Integer rating, @Param("month") Integer month, @Param("year") Integer year);
}
