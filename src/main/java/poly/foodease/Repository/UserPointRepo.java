package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.UserPoint;

import java.util.Optional;

public interface UserPointRepo extends JpaRepository<UserPoint, Integer> {
    @Query("SELECT up FROM UserPoint up JOIN up.user u WHERE u.userName = :userName")
    Optional<UserPoint> getUserPointByUserId(@Param("userName") String userName);

}
