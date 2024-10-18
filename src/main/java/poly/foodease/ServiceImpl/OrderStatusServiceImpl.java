package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.OrderStatusMapper;
import poly.foodease.Model.Entity.OrderStatus;
import poly.foodease.Model.Response.OrderStatusResponse;
import poly.foodease.Repository.OrderStatusRepo;
import poly.foodease.Service.OrderService;
import poly.foodease.Service.OrderStatusService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderStatusServiceImpl implements OrderStatusService {

    @Autowired
    OrderStatusRepo orderStatusRepo;
    @Autowired
    OrderStatusMapper orderStatusMapper;

    @Override
    public List<OrderStatusResponse> getAllOrderStatus() {
        List<OrderStatus> orderStatuses = orderStatusRepo.findAll();
        return orderStatuses.stream()
                .map(orderStatusMapper :: convertEnToRes)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderStatusResponse> getOrderStatusByOrderStatusId(Integer oderStatusId) {
        OrderStatus orderStatus = orderStatusRepo.findById(oderStatusId)
                .orElseThrow(() -> new EntityNotFoundException("not found OrderStatus"));
        return Optional.of(orderStatusMapper.convertEnToRes(orderStatus));
    }
}
