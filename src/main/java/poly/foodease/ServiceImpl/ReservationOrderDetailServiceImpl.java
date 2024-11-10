package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.ReservationOrderDetailMapper;
import poly.foodease.Mapper.ReservationOrderMapper;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Entity.ReservationOrder;
import poly.foodease.Model.Entity.ReservationOrderDetail;
import poly.foodease.Model.Request.ReservationOrderDetailRequest;
import poly.foodease.Model.Request.ReservationOrderRequest;
import poly.foodease.Model.Response.ReservationOrderDetailResponse;
import poly.foodease.Repository.FoodsDao;
import poly.foodease.Repository.ReservationOrderDetailRepo;
import poly.foodease.Repository.ReservationOrderRepo;
import poly.foodease.Repository.ReservationRepo;
import poly.foodease.Service.ReservationOrderDetailService;
import poly.foodease.Service.ReservationOrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservationOrderDetailServiceImpl implements ReservationOrderDetailService {
    @Autowired
    ReservationOrderRepo reservationOrderRepo;
    @Autowired
    private ReservationOrderMapper reservationOrderMapper;
    @Autowired
    private ReservationOrderDetailRepo reservationOrderDetailRepo;
    @Autowired
    private ReservationOrderDetailMapper reservationOrderDetailMapper;
    @Autowired
    private FoodsDao foodsDao;
    @Autowired
    private ReservationRepo reservationRepo;


//    @Override
//    public ReservationOrderDetailResponse createReservationOrderDetail (Integer reservationId, Integer foodId, Integer quantity){
//        ReservationOrder reservationOrder = reservationOrderRepo.getReservationOrderByReservationId(reservationId);
//        Integer reservationOrderId  ;
//        if(reservationOrder != null ){
//            reservationOrderId = reservationOrder.getReservationOrderId();
//        }else {
//            reservationOrder = new ReservationOrder();
//            System.out.println(reservationOrder.getReservationOrderId());
//            reservationOrder.setOrderDateTime(LocalDateTime.now());
//            reservationOrder = reservationOrderRepo.save(reservationOrder);
//            reservationOrderId = reservationOrder.getReservationOrderId();
//            System.out.println("reservationOrderId" + reservationOrderId);
//        }
//        ReservationOrderDetail reservationOrderDetail = reservationOrderDetailRepo.getReservationOrderDetailByFoodId(foodId);
//        if (reservationOrderDetail != null ){
//            reservationOrderDetail.setQuantity( reservationOrderDetail.getQuantity() +quantity);
//            reservationOrderDetail.setPrice( reservationOrderDetail.getFoods().getBasePrice() * reservationOrderDetail.getQuantity());
//            reservationOrder.setTotalPrice(reservationOrder.getTotalPrice() + reservationOrderDetail.getPrice());
//            reservationOrder.setTotalQuantity(reservationOrder.getTotalQuantity() + reservationOrderDetail.getQuantity());
//        }else {
//            ReservationOrderDetailRequest reservationOrderDetailRequest = new ReservationOrderDetailRequest();
//            reservationOrderDetailRequest.setFoodId(foodId);
//            reservationOrderDetailRequest.setReservationOrderId(reservationOrderId);
//            reservationOrderDetailRequest.setQuantity(quantity);
//            Foods foods = foodsDao.findById(foodId).orElseThrow(() -> new EntityNotFoundException("Not found Food"));
//            reservationOrderDetailRequest.setPrice(foods.getBasePrice() + reservationOrderDetailRequest.getQuantity());
//            reservationOrderDetail = reservationOrderDetailMapper.convertReqToEn(reservationOrderDetailRequest);
//            reservationOrder.setTotalPrice(reservationOrder.getTotalPrice() + reservationOrderDetail.getPrice());
//            reservationOrder.setTotalQuantity(reservationOrder.getTotalQuantity() + reservationOrderDetail.getQuantity());
//        }
//        reservationOrderRepo.save(reservationOrder);
//        return reservationOrderDetailMapper.convertEnToRes(reservationOrderDetailRepo.save(reservationOrderDetail));
//    }

    @Override
    public List<ReservationOrderDetailResponse> createReservationOrderDetail(Integer reservationId, Map<Integer, Integer> foodOrderItem) {
        ReservationOrder reservationOrder = reservationOrderRepo.getReservationOrderByReservationId(reservationId);
        List<ReservationOrderDetail> reservationOrderDetails = new ArrayList<>();
        Double totalPrice = 0.0;
        Integer totalQuantity = 0;

        if (reservationOrder == null) {
            reservationOrder = new ReservationOrder();
            reservationOrder.setOrderDateTime(LocalDateTime.now());
            reservationOrder.setStatus(true);
            reservationOrder.setTotalPrice(0.0);
            reservationOrder.setTotalQuantity(0);
            reservationOrder.setReservation(reservationRepo.findById(reservationId)
                    .orElseThrow(() -> new EntityNotFoundException("not found Reservation")));
            reservationOrder = reservationOrderRepo.save(reservationOrder);
        }

        for (Map.Entry<Integer, Integer> foodItem : foodOrderItem.entrySet()) {
            ReservationOrderDetail reservationOrderDetail = reservationOrderDetailRepo.getReservationOrderDetailByFoodId(foodItem.getKey());
            System.out.println("FOOD ID " + foodItem.getKey());
            System.out.println("QUANTITY" + foodItem.getValue());
            if (reservationOrderDetail == null) {
                reservationOrderDetail = new ReservationOrderDetail();
                Foods foods = foodsDao.findById(foodItem.getKey())
                        .orElseThrow(() -> new EntityNotFoundException("not found Food"));
                reservationOrderDetail.setReservationOrder(reservationOrder);
                reservationOrderDetail.setFoods(foods);
                reservationOrderDetail.setQuantity(foodItem.getValue());
                reservationOrderDetail.setPrice(foods.getBasePrice() * reservationOrderDetail.getQuantity());
            } else {
                reservationOrderDetail.setQuantity(reservationOrderDetail.getQuantity() + foodItem.getValue());
                reservationOrderDetail.setPrice(reservationOrderDetail.getQuantity() * reservationOrderDetail.getFoods().getBasePrice());
            }

            reservationOrderDetails.add(reservationOrderDetailRepo.save(reservationOrderDetail));
        }

        // Tính tổng giá trị và tổng số lượng chỉ một lần sau khi thêm tất cả các chi tiết
        totalPrice = reservationOrderDetails.stream().mapToDouble(ReservationOrderDetail::getPrice).sum();
        totalQuantity = reservationOrderDetails.stream().mapToInt(ReservationOrderDetail::getQuantity).sum();

        // Cập nhật reservationOrder chỉ một lần
        reservationOrder.setTotalPrice(reservationOrder.getTotalPrice() + totalPrice);
        reservationOrder.setTotalQuantity(reservationOrder.getTotalQuantity() +totalQuantity);
        reservationOrderRepo.save(reservationOrder);

        return reservationOrderDetails.stream()
                .map(reservationOrderDetailMapper::convertEnToRes)
                .collect(Collectors.toList());
    }

}
