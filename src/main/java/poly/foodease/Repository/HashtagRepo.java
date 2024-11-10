package poly.foodease.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.foodease.Model.Entity.Hashtag;


public interface HashtagRepo extends JpaRepository<Hashtag, Integer>{
    Optional<Hashtag> findByHashtagName(String hashtagName);
}
