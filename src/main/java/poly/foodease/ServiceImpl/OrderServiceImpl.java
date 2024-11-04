package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.foodease.Controller.Controller.WebSocketController;
import poly.foodease.Mapper.OrderMapper;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Model.Request.OrderRequest;
import poly.foodease.Model.Request.PaymentMethodRevenueRequest;
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Model.Response.PaymentMethodRevenueResponse;
import poly.foodease.Report.ReportOrder;
import poly.foodease.Report.ReportRevenueByMonth;
import poly.foodease.Report.ReportUserBuy;
import poly.foodease.Repository.OrderRepo;
import poly.foodease.Repository.OrderStatusRepo;
import poly.foodease.Service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private  OrderRepo orderRepo;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderStatusRepo orderStatusRepo;
    @Autowired
    private WebSocketController webSocketController;
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public Page<OrderResponse> getAllOrder(Integer pageCurrent, Integer pageSize, String sortOrder, String sortBy) {
    logger.info("Fetching all orders - Page: {}, Size: {}, Sort Order: {}, Sort By: {}", pageCurrent, pageSize, sortOrder, sortBy);
    try {
        Sort sort = Sort.by(new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Pageable pageable = PageRequest.of(pageCurrent, pageSize, sort);
        Page<Order> orderPage = orderRepo.findAll(pageable);
        List<OrderResponse> orders = orderPage.getContent().stream()
                .map(orderMapper::convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(orders, pageable, orderPage.getTotalPages());
    } catch (Exception e) {
        logger.error("Error fetching orders", e);
        throw e;
    }
    }

    @Override
    public Optional<OrderResponse> getOrderByOrderId(Integer orderId) {
    logger.info("Fetching order by ID: {}", orderId);
    try {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Not found Order"));
        return Optional.of(orderMapper.convertEnToRes(order));
    } catch (EntityNotFoundException e) {
        logger.error("Order not found with ID: {}", orderId, e);
        throw e;
    }
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
    logger.info("Creating new order with request: {}", orderRequest);
    try {
        Order order = orderMapper.convertReqToEn(orderRequest);
        Order orderCreated = orderRepo.save(order);
        logger.info("Order created successfully with ID: {}", orderCreated.getOrderId());
        return orderMapper.convertEnToRes(orderCreated);
    } catch (Exception e) {
        logger.error("Error creating order", e);
        throw e;
    }
    }

    @Override
    public Optional<OrderResponse> updateOrder(Integer orderId, OrderRequest orderRequest) {
    logger.info("Updating order with ID: {} and request: {}", orderId, orderRequest);
    try {
        return Optional.of(orderRepo.findById(orderId).map(orderExists -> {
            Order order = orderMapper.convertReqToEn(orderRequest);
            order.setOrderId(orderExists.getOrderId());
            Order orderUpdated = orderRepo.save(order);
            logger.info("Order updated successfully with ID: {}", orderId);
            return orderMapper.convertEnToRes(orderUpdated);
        }).orElseThrow(() -> new EntityNotFoundException("Order not found")));
    } catch (EntityNotFoundException e) {
        logger.error("Order not found with ID: {}", orderId, e);
        throw e;
    } catch (Exception e) {
        logger.error("Error updating order with ID: {}", orderId, e);
        throw e;
    }
    }

    @Override
    public Page<OrderResponse> getOrderByUserName(String userName,Integer pageCurrent, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort = Sort.by(new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Pageable pageable = PageRequest.of(pageCurrent, pageSize, sort);
        Page<Order> orderPage = orderRepo.getOrdersByUserName(userName,pageable);
        List<OrderResponse> orders= orderPage.getContent().stream()
                .map(orderMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(orders,pageable,orderPage.getTotalElements());
    }

    @Override
    public Page<OrderResponse> getOrderByStatusId(String userName, Integer orderStatusId,Integer pageCurrent, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort = Sort.by(new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC , sortBy));
        Pageable pageable = PageRequest.of(pageCurrent,pageSize,sort);
        Page<Order> orderPage = orderRepo.getOrderByStatusId(userName, orderStatusId, pageable);
        List<OrderResponse> orders = orderPage.getContent().stream()
                .map(orderMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(orders,pageable , orderPage.getTotalElements());
    }

    @Override
    public OrderResponse changeOrderStatus(Integer orderId,Integer orderStatusId){
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Not found order"));
        order.setOrderStatus(orderStatusRepo.findById(orderStatusId)
                .orElseThrow(() -> new EntityNotFoundException("Not found OrderStatus By Id")));
        return orderMapper.convertEnToRes(orderRepo.save(order));
    }

    // Hàm này giúp tự động cập nhật trạng thái đơn hàng
    @Override
    @Scheduled(fixedRate = 60000)
    @Transactional
    public List<OrderResponse> changeOrderStatusToDelived() {
    logger.info("Scheduled update for changing order statuses to delivered");
    try {
        List<Integer> orderStatusIds = Arrays.asList(2, 3, 4);
        LocalDateTime now = LocalDateTime.now();
        List<Order> orders = orderRepo.getOrdersToUpdate(orderStatusIds);
        logger.info("Total orders to update: {}", orders.size());

        orders.forEach(order -> {
            try {
                if (order.getOrderStatus().getOrderStatusId() == 2) {
                    LocalDateTime paymentDateTime = order.getPaymentDatetime();
                    if (paymentDateTime.plusMinutes(3).isBefore(now)) {
                        logger.info("Order ID: {} status changing to Shipping", order.getOrderId());
                        order.setOrderStatus(orderStatusRepo.findById(3).orElseThrow());
                        webSocketController.sendOrderUpdateMessage("Order " + order.getOrderId() + " is now Shipping.");
                    }
                } else if (order.getOrderStatus().getOrderStatusId() == 3) {
                    LocalDateTime estimatedDateTime = order.getEstimatedDeliveryDateTime();
                    if (estimatedDateTime.isBefore(now)) {
                        logger.info("Order ID: {} status changing to Delivered", order.getOrderId());
                        order.setOrderStatus(orderStatusRepo.findById(4).orElseThrow());
                        webSocketController.sendOrderUpdateMessage("Order " + order.getOrderId() + " is now Delivered.");
                    }
                }
            } catch (Exception e) {
                logger.error("Error updating status for order ID: {}", order.getOrderId(), e);
            }
        });

        return orderRepo.saveAll(orders).stream()
                .map(orderMapper::convertEnToRes)
                .toList();
    } catch (Exception e) {
        logger.error("Error in scheduled update for changing order statuses", e);
        throw e;
    }
    }

    // Ngọc
    @Override
    public List<OrderResponse> findAll() {
        List<Order> list=orderRepo.findAll();
        return list.stream().map(orderMapper :: convertEnToRes)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportOrder> findTotalPriceAndQuantityByOrderDate() {
        // TODO Auto-generated method stub
        return orderRepo.findTotalPriceAndQuantityByOrderDate();
    }

    @Override
    public List<ReportRevenueByMonth> getRevenueByMonth() {
        // TODO Auto-generated method stub
        return orderRepo.getRevenueByMonth();
    }

    @Override
    public List<poly.foodease.Report.ReportRevenueByYear> ReportRevenueByYear() {
        // TODO Auto-generated method stub
        return orderRepo.ReportRevenueByYear();
    }


    @Override
    public List<PaymentMethodRevenueResponse> getRevenueByPaymentMethod(PaymentMethodRevenueRequest request) {
        return orderRepo.getRevenueByPaymentMethod(request.getYear(), request.getMonth(),
                request.getStartDate(), request.getEndDate());
    }

    @Override
    public Order getOrderById(Integer orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));
    }


	@Override
	public Page<OrderResponse> findOrderByOrderDate(LocalDate date, Pageable page) {
		// TODO Auto-generated method stub
		Page<Order> list=orderRepo.findOrderByOrderDate(date, page);
		
		return list.map(orderMapper :: convertEnToRes);
	}

	@Override
	public Page<ReportOrder> ReportRevenueByToday(LocalDate date, Pageable page) {
		// TODO Auto-generated method stub
		return orderRepo.ReportRevenueByToday(date, page);
	}

	@Override
	public Page<ReportUserBuy> findReportUserBuy(LocalDate date, Pageable page) {
		// TODO Auto-generated method stub
		return orderRepo.findReportUserBuy(date, page);
	}

	@Override
	public List<ReportUserBuy> findAllReportUserBuy() {
		// TODO Auto-generated method stub
		return orderRepo.findAllReportUserBuy();
	}

}
