package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.FoodCategories;


public interface FoodCategoryDao extends JpaRepository<FoodCategories, Integer>{

}
