package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.OrderReturn;

import java.util.List;

public interface OrderReturnRepo extends JpaRepository<OrderReturn, Integer> {

    @Query("SELECT ore FROM OrderReturn ore JOIN ore.order o " +
            " WHERE o.orderId= :orderId")
    List<OrderReturn> getOrderReturnByOrderId(@Param("orderId") Integer orderId );
}
