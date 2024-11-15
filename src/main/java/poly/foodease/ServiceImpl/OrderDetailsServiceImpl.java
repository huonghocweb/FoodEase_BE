package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.OrderDetailsMapper;
import poly.foodease.Model.Entity.OrderDetails;
import poly.foodease.Model.Request.OrderDetailsRequest;
import poly.foodease.Model.Response.OrderDetailsResponse;
import poly.foodease.Report.FoodBuyMost;
import poly.foodease.Repository.OrderDetailsRepo;
import poly.foodease.Repository.OrderRepo;
import poly.foodease.Service.OrderDetailsService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    @Autowired
    private OrderDetailsRepo orderDetailsRepo;
    @Autowired
    private OrderDetailsMapper orderDetailsMapper;
    @Autowired
    private OrderRepo orderRepo;


    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @Override
    public Page<OrderDetailsResponse> getAllOrderDetails(Integer pageCurrent, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort = Sort.by(new Sort.Order(Objects.equals(sortOrder, "sortOrder") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Pageable pageable = PageRequest.of(pageCurrent, pageSize, sort);
        Page<OrderDetails> pageOrderDetails = orderDetailsRepo.findAll(pageable);
        List<OrderDetailsResponse> orderDetails = pageOrderDetails.getContent().stream()
                .map(orderDetailsMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(orderDetails,pageable,pageOrderDetails.getTotalElements());
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF' , 'USER')")
    @Override
    public Optional<OrderDetailsResponse> getOrderDetails(Integer orderDetailsId) {
        return Optional.of(orderDetailsRepo.findById(orderDetailsId)
                .map(orderDetailsMapper :: convertEnToRes))
                .orElseThrow(() -> new EntityNotFoundException("Not found OrderDetails"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF', 'USER')")
    @Override
    public List<OrderDetailsResponse> getOrderDetailsByOrderId(Integer orderId) {
        List<OrderDetailsResponse> orderDetails = orderDetailsRepo.getOrderDetailsByOrderId(orderId).stream()
                .map(orderDetailsMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return orderDetails;
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF', 'USER')")
    @Override
    public OrderDetailsResponse createOrderDetails(OrderDetailsRequest orderDetailsRequest) {
        OrderDetails orderDetails = orderDetailsMapper.convertReqToEn(orderDetailsRequest);
        OrderDetails orderDetailsCreated = orderDetailsRepo.save(orderDetails);
        return orderDetailsMapper.convertEnToRes(orderDetailsCreated);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF', 'USER')")
    @Override
    public Optional<OrderDetailsResponse> updateOrderDetails(Integer orderDetailsId, OrderDetailsRequest orderDetailsRequest) {
        return Optional.of(orderDetailsRepo.findById(orderDetailsId).map(orderDetailsExists -> {
            OrderDetails orderDetails = orderDetailsMapper.convertReqToEn(orderDetailsRequest);
            orderDetails.setOrderDetailsId(orderDetailsExists.getOrderDetailsId());
            OrderDetails orderDetailsUpdated= orderDetailsRepo.save(orderDetails);
            return orderDetailsMapper.convertEnToRes(orderDetailsUpdated);
        }))
                .orElseThrow(() -> new EntityNotFoundException("Not found OrderDetails"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF', 'USER')")
    @Override
    public List<OrderDetails> findByOrderId(Integer orderId) {
        return orderDetailsRepo.getOrderDetailsByOrderId(orderId);
    }

	@Override
	public Page<poly.foodease.Report.FoodBuyMost> FoodBuyMost(Pageable page) {
		// TODO Auto-generated method stub
		Page<FoodBuyMost> list=orderDetailsRepo.FoodBuyMost(page);
		return list;
	}

	@Override
	public poly.foodease.Report.FoodBuyMost FoodSold(Integer foodId) {
		// TODO Auto-generated method stub
		FoodBuyMost list=orderDetailsRepo.FoodSold(foodId);
		return list;
	}

	
}
