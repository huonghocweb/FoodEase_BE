package poly.foodease.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.CouponStorage;

import java.util.List;

public interface CouponStorageRepo extends JpaRepository<CouponStorage, Integer> {
    @Query("SELECT cps FROM CouponStorage  cps JOIN cps.user u WHERE u.userName = :userName")
    Page<CouponStorage> getCouponStorageByUserName(@Param("userName") String userName , Pageable pageable);
    @Query("SELECT cps FROM CouponStorage  cps JOIN cps.user u WHERE u.userName = :userName")
    List<CouponStorage> getAllCouponStorageByUserName(@Param("userName") String userName );
    @Query("SELECT cps FROM CouponStorage cps JOIN cps.user u JOIN cps.coupon cp " +
            " WHERE u.userName= :userName AND cp.couponId=:couponId ")
    CouponStorage getCouponStorageByUserNameAndCouponId(@Param("userName") String userName,
                                                        @Param("couponId") Integer couponId);
}
