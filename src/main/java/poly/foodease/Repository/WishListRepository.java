package poly.foodease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poly.foodease.Model.Entity.WishList;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {
    List<WishList> findByUserUserId(Integer userId);

    WishList findByWishListId(Integer wishListId);
}
