package poly.foodease.Service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Request.OrderReturnRequest;
import poly.foodease.Model.Response.OrderReturnResponse;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderReturnService {
    Page<OrderReturnResponse> getAllOrderReturn(Integer pageCurrent, Integer pageSize, String orderBy , String sortBy);
    List<OrderReturnResponse> getOrderReturnByOrderId(Integer orderId);
    List<OrderReturnResponse> createOrderReturn(OrderReturnRequest orderReturnRequest);
    List<OrderReturnResponse> approveOrderReturnRequest(Integer orderId,Boolean approve);
    Optional<OrderReturnResponse> updateOrderReturn(Integer oderReturnId, OrderReturnRequest orderReturnRequest);
}
