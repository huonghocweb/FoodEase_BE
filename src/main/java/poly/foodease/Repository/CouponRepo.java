package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.Coupon;

import java.util.Optional;

public interface CouponRepo extends JpaRepository<Coupon,Integer> {
    @Query("SELECT cp FROM Coupon  cp WHERE cp.code = :code")
    Optional<Coupon> findCouponByCode(@Param("code") String code);
}
