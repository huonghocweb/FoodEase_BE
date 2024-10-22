package poly.foodease.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Model.Entity.OrderDetails;
import poly.foodease.Model.Entity.OrderStatus;

import java.util.List;

public interface OrderDetailsRepo extends JpaRepository<OrderDetails, Integer> {
    @Query("SELECT od FROM OrderDetails od JOIN od.order o WHERE o.orderId = :orderId")
    List<OrderDetails> getOrderDetailsByOrderId(@Param("orderId") Integer orderId);

    List<OrderDetails> findByOrder(Order order);

}
