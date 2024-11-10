package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.OrderMapper;
import poly.foodease.Mapper.OrderReturnMapper;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Model.Entity.OrderDetails;
import poly.foodease.Model.Entity.OrderReturn;
import poly.foodease.Model.Entity.UserPoint;
import poly.foodease.Model.Request.OrderRequest;
import poly.foodease.Model.Request.OrderReturnRequest;
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Model.Response.OrderReturnResponse;
import poly.foodease.Repository.OrderDetailsRepo;
import poly.foodease.Repository.OrderRepo;
import poly.foodease.Repository.OrderReturnRepo;
import poly.foodease.Service.OrderDetailsService;
import poly.foodease.Service.OrderReturnService;
import poly.foodease.Service.OrderService;
import poly.foodease.Service.UserPointService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderReturnServiceImpl implements OrderReturnService {
    @Autowired
    OrderReturnRepo orderReturnRepo;
    @Autowired
    OrderReturnMapper orderReturnMapper;
    @Autowired
    OrderDetailsService orderDetailsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailsRepo orderDetailsRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private UserPointService userPointService;
    @Override
    public Page<OrderReturnResponse> getAllOrderReturn(Integer pageCurrent, Integer pageSize, String orderBy, String sortBy) {
        Sort sort = Sort.by(new Sort.Order(Objects.equals(orderBy, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Pageable pageable = PageRequest.of(pageCurrent, pageSize, sort);
        Page<OrderReturn> orderReturns = orderReturnRepo.findAll(pageable);
        List<OrderReturnResponse> orderReturnResponses = orderReturns.getContent().stream()
                .map(orderReturnMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(orderReturnResponses,pageable,orderReturns.getTotalElements());
    }

    @Override
    public List<OrderReturnResponse> getOrderReturnByOrderId(Integer orderId) {
        List<OrderReturn> orderReturns = orderReturnRepo.getOrderReturnByOrderId(orderId);
        return orderReturns.stream()
                .map(orderReturnMapper :: convertEnToRes)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderReturnResponse> createOrderReturn(OrderReturnRequest orderReturnRequest) {
        Integer orderId = orderReturnRequest.getOrderId();
        List<OrderReturnResponse> orderReturnResponse = orderDetailsRepo.getOrderDetailsByOrderId(orderId).stream().map(orderDetail -> {
                Integer foodVaId = orderDetail.getFoodVariations().getFoodVariationId();
                orderReturnRequest.setFoodVaId(foodVaId);
                OrderReturn orderReturn = orderReturnMapper.convertReqToEn(orderReturnRequest);
                OrderReturn orderReturnCreated = orderReturnRepo.save(orderReturn);
                return  orderReturnMapper.convertEnToRes(orderReturnCreated);
                })
                .collect(Collectors.toList());
        orderService.changeOrderStatus(orderId, 5);
        System.out.println("Success");
        return orderReturnResponse;
    }

    @Override
    public List<OrderReturnResponse> approveOrderReturnRequest(Integer orderId,Boolean approve) {
        // Hàm xử lý khi admin phê duyệt Return
        // Nếu approve = true, cập nhật đơn hàng thành Returned, chuyển trạng thái orderReturn thành refund
        // Nếu approve = flase , chuyển đơn hàng về trạng thái Success! , chuyển trạng thái của orderReturn từ pending thành cancel
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Not Found Order"));
        List<OrderReturn>  orderReturns = orderReturnRepo.getOrderReturnByOrderId(orderId);
        return orderReturns.stream().map(orderReturn -> {
                if(approve){
                    orderReturn.setStatus("Refund");
                    orderService.changeOrderStatus(orderId,7 );
                    // Hoàn tiền thành point
                    userPointService.updatePointByUserName(order.getUser().getUserName(), order.getTotalPrice(), false);
                }else{
                    orderReturn.setStatus("Cancel");
                    orderService.changeOrderStatus(orderId,6 );
                }
                OrderReturn orderReturnUpdate= orderReturnRepo.save(orderReturn);
                return orderReturnMapper.convertEnToRes(orderReturnUpdate);
            })
                    .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderReturnResponse> updateOrderReturn(Integer oderReturnId, OrderReturnRequest orderReturnRequest) {
        return orderReturnRepo.findById(oderReturnId).map(orderReturnExists -> {
            OrderReturn orderReturn = orderReturnMapper.convertReqToEn(orderReturnRequest);
            orderReturn.setOrderReturnId(orderReturnExists.getOrderReturnId());
            OrderReturn orderReturnUpdated = orderReturnRepo.save(orderReturn);
            return orderReturnMapper.convertEnToRes(orderReturnUpdated);
        });
    }
}
