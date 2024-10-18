package poly.foodease.Mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Model.Entity.OrderDetails;
import poly.foodease.Model.Request.OrderRequest;
import poly.foodease.Model.Response.OrderDetailsResponse;
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {
    @Autowired
    CouponMapper couponMapper;
    @Autowired
    OrderStatusMapper orderStatusMapper;
    @Autowired
    ShipMethodMapper shipMethodMapper;
    @Autowired
    PaymentMethodMapper paymentMethodMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    OrderDetailsMapper orderDetailsMapper;
    @Autowired
    ShipMethodRepo shipMethodRepo;
    @Autowired
    PaymentMethodRepo paymentMethodRepo;
    @Autowired
    private OrderDetailsRepo orderDetailsRepo;
    @Autowired
    private OrderStatusRepo orderStatusRepo;
    @Autowired
    private CouponRepo couponRepo;
    @Autowired
    private UserRepo userRepo;


    public OrderResponse convertEnToRes(Order order){
            return OrderResponse.builder()
                    .orderId(order.getOrderId())
                    .orderDate(order.getOrderDate())
                    .orderTime(order.getOrderTime())
                    .coupon(order.getCoupon() != null ? couponMapper.convertEnToResponse(order.getCoupon()) : null)
                    .orderStatus(orderStatusMapper.convertEnToRes(order.getOrderStatus()))
                    .shipMethod(shipMethodMapper.convertEnToRes(order.getShipMethod()))
                    .deliveryAddress(order.getDeleveryAddress())
                    .shipFee(order.getShipFee())
                    .leadTime(order.getLeadTime())
                    .paymentDateTime(order.getPaymentDatetime())
                    .estimatedDeliveryDatTime(order.getEstimatedDeliveryDateTime())
                    .orderDetails(order.getOrderDetails() != null ?
                            order.getOrderDetails().stream()
                                    .map(orderDetailsMapper :: convertEnToRes)
                                    .collect(Collectors.toList()) : null)
                    .paymentMethod(paymentMethodMapper.convertEnToRes(order.getPaymentMethod()))
                    .user(userMapper.convertEnToRes(order.getUser()))
                    .totalPrice(order.getTotalPrice())
                    .totalQuantity(order.getTotalQuantity())
                    .updateAt(order.getUpdateAt())
                    .build();
        }
        public Order convertReqToEn(OrderRequest orderRequest){
            return Order.builder()
                    .orderDate(orderRequest.getOrderDate() != null ? orderRequest.getOrderDate() : LocalDate.now())
                    .orderTime(orderRequest.getOrderTime() != null ? orderRequest.getOrderTime() : LocalTime.now())
                    .deleveryAddress(orderRequest.getDeliveryAddress())
                    .totalPrice(orderRequest.getTotalPrice())
                    .totalQuantity(orderRequest.getTotalQuantity())
                    .updateAt(LocalDateTime.now())
                    .shipFee(orderRequest.getShipFee())
                    .leadTime(orderRequest.getLeadTime())
                    .paymentDatetime(orderRequest.getPaymentDateTime())
                    .estimatedDeliveryDateTime(orderRequest.getEstimatedDeliveryDateTime())
                    .paymentMethod(paymentMethodRepo.findById(orderRequest.getPaymentMethodId())
                            .orElseThrow(() -> new EntityNotFoundException("Not Found Payment Method")))
                    .orderStatus(orderStatusRepo.findById(orderRequest.getOrderStatusId())
                            .orElseThrow(() -> new EntityNotFoundException("Not Found Order Status")))
                   .coupon(orderRequest.getCouponId() != null ? couponRepo.findById(orderRequest.getCouponId())
                            .orElseThrow(()-> new EntityNotFoundException("Not Found Coupon ")) : null)
                    .shipMethod(shipMethodRepo.findById(orderRequest.getShipMethodId())
                            .orElseThrow(() -> new EntityNotFoundException("Not found shipMethod")))
                    .user(userRepo.findById(orderRequest.getUserId())
                            .orElseThrow(() -> new EntityNotFoundException("Not found User")))
//                    .orderDetails(orderRequest.getOrderDetailsId() != null ?
//                            orderRequest.getOrderDetailsId().stream()
//                                    .map(oderDetailsId -> orderDetailsRepo.findById(oderDetailsId)
//                                            .orElseThrow(() -> new EntityNotFoundException("Not found OrderDetails")))
//                                    .collect(Collectors.toList()) : null
//                                    )
                    .build();
        }

        public OrderRequest convertResToReq(OrderResponse orderResponse){
            return OrderRequest.builder()
                    .totalPrice(orderResponse.getTotalPrice())
                    .totalQuantity(orderResponse.getTotalQuantity())
                    .deliveryAddress(orderResponse.getDeliveryAddress())
                    .orderDate(orderResponse.getOrderDate())
                    .orderTime(orderResponse.getOrderTime())
                    .shipFee(orderResponse.getShipFee())
                    .leadTime(orderResponse.getLeadTime())
                    .paymentDateTime(orderResponse.getEstimatedDeliveryDatTime())
                    .estimatedDeliveryDateTime(orderResponse.getEstimatedDeliveryDatTime())
                    .userId(orderResponse.getUser().getUserId())
                    .couponId(orderResponse.getCoupon() != null ?orderResponse.getCoupon().getCouponId() : null)
                    .shipMethodId(orderResponse.getShipMethod().getShipId())
                    .paymentMethodId(orderResponse.getPaymentMethod().getPaymentId())
                    .orderStatusId(orderResponse.getOrderStatus().getOrderStatusId())
//                    .orderDetailsId(orderResponse.getOrderDetails() != null ?
//                            orderResponse.getOrderDetails().stream()
//                                    .map(OrderDetailsResponse::getOrderDetailsId)
//                                    .collect(Collectors.toList()) : null)
                    .build();
        }
}
