package poly.foodease.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import poly.foodease.Model.Entity.FoodReview;
import poly.foodease.Report.FoodRating;
import poly.foodease.Report.Rating;

public interface FoodReviewDao extends JpaRepository<FoodReview, Integer> {
	@Query("SELECT fr FROM FoodReview fr where fr.foodId = ?1")
	List<FoodReview> findFoodReviewByFoodId(Integer id);

	@Query("SELECT new poly.foodease.Report.Rating(AVG(fr.rating), fr.foodId) FROM FoodReview fr where fr.foodId = ?1 GROUP BY fr.foodId")
	Rating AVGRating(Integer foodId);

	@Query("SELECT new poly.foodease.Report.FoodRating(AVG(fr.rating) as avgRating, fr.food) FROM FoodReview fr GROUP BY fr.foodId,fr.food")
	Page<FoodRating> FoodRating(Pageable page);

@Query("SELECT o FROM FoodReview o  WHERE (:rating IS NULL OR o.rating = :rating) AND (FUNCTION('MONTH', o.reviewDate) = :month) AND (FUNCTION('YEAR', o.reviewDate) = :year)")
List<FoodReview> findByFilter(@Param("rating") Integer rating, @Param("month") Integer month, @Param("year") Integer year);
}
