package poly.foodease.Service;

import org.springframework.stereotype.Service;
import poly.foodease.Model.Response.OrderStatusResponse;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderStatusService {
    List<OrderStatusResponse> getAllOrderStatus();
    Optional<OrderStatusResponse> getOrderStatusByOrderStatusId(Integer oderStatusId);
}
