package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    Optional<User> findUserByUserName(@Param("userName") String userName);
    //    Hòa
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findTopByEmail(String email);
//    Hòa
}
