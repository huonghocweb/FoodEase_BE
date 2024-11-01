package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.foodease.Model.Entity.BlogCategory;


public interface BlogCategoryRepo extends JpaRepository<BlogCategory, Integer>{
}
