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
import poly.foodease.Model.Entity.TableServices;
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
        // Lấy ra order của bàn hiện tại theo mã đặt bàn
        ReservationOrder reservationOrder = null;
        reservationOrder =  reservationOrderRepo.getReservationOrderByReservationId(reservationId);

        List<ReservationOrderDetail> reservationOrderDetails = new ArrayList<>();
        Double totalPrice = 0.0;
        Integer totalQuantity = 0;
        Double totalService = 0.0;
        System.out.println(foodOrderItem);
      //  System.out.println("Mã Order " + reservationOrder.getReservationOrderId());
        if (reservationOrder == null) {
            // Nếu order của bàn chưa có , tạo order mới, set các giá trị mặc định là 0 .
            System.out.println("Tao moi Order Cho ban");
            reservationOrder = new ReservationOrder();
            reservationOrder.setOrderDateTime(LocalDateTime.now());
            reservationOrder.setStatus(true);
            reservationOrder.setTotalPrice(0.0);
            reservationOrder.setTotalQuantity(0);
            reservationOrder.setTotalServicePrice(totalService);
            reservationOrder.setReservation(reservationRepo.findById(reservationId)
                    .orElseThrow(() -> new EntityNotFoundException("not found Reservation")));
            reservationOrder = reservationOrderRepo.save(reservationOrder);
        }

        for (Map.Entry<Integer, Integer> foodItem : foodOrderItem.entrySet()) {
            // Lấy ra orderDetails theo foodId và mã order, nếu chưa có tạo mới
            ReservationOrderDetail reservationOrderDetail = reservationOrderDetailRepo.getReservationOrderDetailByFoodId(foodItem.getKey(), reservationOrder.getReservationOrderId());
            System.out.println("FOOD ID " + foodItem.getKey());
            System.out.println("QUANTITY " + foodItem.getValue());
            Foods foods = foodsDao.findById(foodItem.getKey())
                    .orElseThrow(() -> new EntityNotFoundException("not found Food"));
            if (reservationOrderDetail == null) {
                System.out.println("Tạo mới Order Details");
                reservationOrderDetail = new ReservationOrderDetail();
                reservationOrderDetail.setReservationOrder(reservationOrder);
                reservationOrderDetail.setFoods(foods);
                reservationOrderDetail.setQuantity(foodItem.getValue());
                reservationOrderDetail.setPrice(foods.getBasePrice() * reservationOrderDetail.getQuantity());
//                totalPrice += foods.getBasePrice() * foodItem.getValue();
//                totalQuantity +=foodItem.getValue();
            } else {
                // Nếu orderDetails đã có , cập nhật quantity và price
                System.out.println("Da co Order Detail");
                reservationOrderDetail.setQuantity(reservationOrderDetail.getQuantity() + foodItem.getValue());
                reservationOrderDetail.setPrice(reservationOrderDetail.getQuantity() * reservationOrderDetail.getFoods().getBasePrice());

            }
            // Tính số lượng và tổng tiền mà đã thêm mới vào giỏ
            totalPrice += foods.getBasePrice() * foodItem.getValue();
            totalQuantity +=foodItem.getValue();
            reservationOrderDetails.add(reservationOrderDetailRepo.save(reservationOrderDetail));
        }
        System.out.println("reservationOrderDetails : " + reservationOrderDetails.size());

        // Tính tổng giá trị và tổng số lượng chỉ một lần sau khi thêm tất cả các chi tiết
       //totalPrice = reservationOrderDetails.stream().mapToDouble(ReservationOrderDetail::getPrice).sum();
//totalQuantity = reservationOrderDetails.stream().mapToInt(ReservationOrderDetail::getQuantity).sum();
        System.out.println("ToTal Quantity " + totalQuantity);
        System.out.println("ToTal Price " + totalPrice);
        System.out.println("reservationOrder Quantity : " + reservationOrder.getTotalQuantity());
        System.out.println("reservationOrder TotalPrice : " + reservationOrder.getTotalPrice());
         totalService = reservationOrder.getReservation().getServices().stream().mapToDouble(TableServices::getServicePrice).sum();
        // Cập nhật reservationOrder
        // Lấy quantity và price cũ cộng với quantity và price mới thêm vào order
        reservationOrder.setTotalServicePrice(totalService);
        reservationOrder.setTotalPrice( reservationOrder.getTotalPrice() +  totalPrice );
        reservationOrder.setTotalQuantity( reservationOrder.getTotalQuantity() +  totalQuantity);
        reservationOrderRepo.save(reservationOrder);

        return reservationOrderDetails.stream()
                .map(reservationOrderDetailMapper::convertEnToRes)
                .collect(Collectors.toList());
    }

}
