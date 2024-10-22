package poly.foodease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poly.foodease.Model.Entity.WishList;

@Repository
public interface WishListRepo extends JpaRepository<WishList, Integer> {
    List<WishList> findAllByUser_UserId(Integer userId);
}
