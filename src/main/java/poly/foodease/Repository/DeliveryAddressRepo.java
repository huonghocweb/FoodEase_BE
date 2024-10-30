package poly.foodease.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.DeliveryAddress;

public interface DeliveryAddressRepo extends JpaRepository<DeliveryAddress,Integer> {

    @Query("SELECT deli FROM DeliveryAddress deli WHERE deli.user.userName = :userName")
    Page<DeliveryAddress> getDeliveryAddressByUserName(@Param("userName") String userName, Pageable pageable);
}
