package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.foodease.Model.Entity.BlogAuthor;


public interface BlogAuthorRepo extends JpaRepository<BlogAuthor, Integer>{
}
