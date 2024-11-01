package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.foodease.Model.Entity.Hashtag;


public interface HashtagRepo extends JpaRepository<Hashtag, Integer>{
}
