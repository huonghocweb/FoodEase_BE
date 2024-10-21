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
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Report.ReportOrder;
import poly.foodease.Report.ReportRevenueByMonth;
import poly.foodease.Report.ReportUserBuy;
import poly.foodease.Repository.OrderRepo;
import poly.foodease.Repository.OrderStatusRepo;
import poly.foodease.Service.OrderService;

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

    @Override
    public Page<OrderResponse> getAllOrder(Integer pageCurrent, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort = Sort.by(new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC,sortBy ));
        Pageable pageable = PageRequest.of(pageCurrent,pageSize,sort);
        Page<Order> orderPage = orderRepo.findAll(pageable);
        List<OrderResponse> orders = orderPage.getContent().stream()
                .map(orderMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(orders,pageable, orderPage.getTotalPages());
    }

    @Override
    public Optional<OrderResponse> getOrderByOrderId(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Not found Order"));
        return Optional.of(orderMapper.convertEnToRes(order));
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = orderMapper.convertReqToEn(orderRequest);
        Order orderCreated = orderRepo.save(order);
        System.out.println("Create Order");
        return orderMapper.convertEnToRes(orderCreated);
    }

    @Override
    public Optional<OrderResponse> updateOrder(Integer orderId, OrderRequest orderRequest) {
        return Optional.of(orderRepo.findById(orderId).map(orderExists -> {
            Order order = orderMapper.convertReqToEn(orderRequest);
            order.setOrderId(orderExists.getOrderId());
            Order orderUpdated= orderRepo.save(order);
            return orderMapper.convertEnToRes(orderUpdated);
        })
                .orElseThrow(() -> new EntityNotFoundException("not found Order")));
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
    public List<OrderResponse> changeOrderStatusToDelived(){
        List<Integer> orderStatusIds= Arrays.asList(2,3,4);
        LocalDateTime now = LocalDateTime.now();
        List<Order> orders = orderRepo.getOrdersToUpdate(orderStatusIds);
        System.out.println("Total Order Update " +orders.size());
        webSocketController.sendOrderUpdateMessage("Time : " + LocalDateTime.now());
        webSocketController.sendOrderUpdateMessage("Total Order Need  Update  : " +orders.size() );
        orders.forEach(order -> {
            if(order.getOrderStatus().getOrderStatusId() == 2){
                LocalDateTime paymentDateTime = order.getPaymentDatetime();
                if(paymentDateTime.plusMinutes(3).isBefore(now)){
                    System.out.println("The Order Status is change to Shipping");
                    order.setOrderStatus(orderStatusRepo.findById(3).orElseThrow());
                    webSocketController.sendOrderUpdateMessage("Time : " + LocalDateTime.now());
                    webSocketController.sendOrderUpdateMessage("The Order " + order.getOrderId() + " is now Shipping." );
                }
            }else if(order.getOrderStatus().getOrderStatusId() == 3){
                LocalDateTime estimatedDateTime = order.getEstimatedDeliveryDateTime();
                if(estimatedDateTime.isBefore(now)){
                    System.out.println("The Order Status is change to Delivered");
                    order.setOrderStatus(orderStatusRepo.findById(4).orElseThrow());
                    webSocketController.sendOrderUpdateMessage("Time : " + LocalDateTime.now());
                    webSocketController.sendOrderUpdateMessage("The Order " + order.getOrderId() + " is now Delivered." );
                }
            }else if(order.getOrderStatus().getOrderStatusId() == 4){
                LocalDateTime estimatedDateTime = order.getEstimatedDeliveryDateTime();
                if(estimatedDateTime.plusDays(15).isBefore(now)){
                    System.out.println("The Order is Complete");
                    order.setOrderStatus(orderStatusRepo.findById(6).orElseThrow());
                    webSocketController.sendOrderUpdateMessage("Time : " + LocalDateTime.now());
                    webSocketController.sendOrderUpdateMessage("The Order " + order.getOrderId() + " is now Complete." );
                }
            }
        });
        return orderRepo.saveAll(orders).stream()
                .map(orderMapper :: convertEnToRes)
                .toList();
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


}
