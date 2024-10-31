package poly.foodease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.foodease.Model.Entity.Blog;


public interface BlogRepo extends JpaRepository<Blog, Integer>{
    List<Blog> findByBlogCategory_BlogCategoryId(Integer categoryId);
}
