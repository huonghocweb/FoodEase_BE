package poly.foodease.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.OrderDetails;
import poly.foodease.Model.Request.OrderDetailsRequest;
import poly.foodease.Model.Response.OrderDetailsResponse;
import poly.foodease.Report.FoodBuyMost;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderDetailsService {
    Page<OrderDetailsResponse> getAllOrderDetails(Integer pageCurrent, Integer pageSize, String sortOrder, String sortBy);
    Optional<OrderDetailsResponse> getOrderDetails(Integer orderDetailsId);
    List<OrderDetailsResponse> getOrderDetailsByOrderId(Integer orderId);
    OrderDetailsResponse createOrderDetails(OrderDetailsRequest orderDetailsRequest);
    Optional<OrderDetailsResponse> updateOrderDetails(Integer orderDetailsId, OrderDetailsRequest orderDetailsRequest);

    List<OrderDetails> findByOrderId(Integer orderId);
    Page<FoodBuyMost> FoodBuyMost(Pageable page);
    FoodBuyMost FoodSold(Integer foodId);
}
