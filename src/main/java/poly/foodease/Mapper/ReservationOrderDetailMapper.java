package poly.foodease.Mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import poly.foodease.Model.Entity.ReservationOrderDetail;
import poly.foodease.Model.Request.ReservationOrderDetailRequest;
import poly.foodease.Model.Response.ReservationOrderDetailResponse;
import poly.foodease.Repository.FoodsDao;
import poly.foodease.Repository.ReservationOrderRepo;

@Mapper(componentModel = "spring")
public abstract class ReservationOrderDetailMapper {
    @Autowired
    private FoodMapper foodMapper;
    @Autowired
     private FoodsDao foodsDao;
    @Autowired
    private ReservationOrderRepo reservationOrderRepo;

        public ReservationOrderDetailResponse convertEnToRes(ReservationOrderDetail reservationOrderDetail){
            return ReservationOrderDetailResponse.builder()
                    .reservationOrderDetailId(reservationOrderDetail.getReservationOrderDetailId())
                    .price(reservationOrderDetail.getPrice())
                    .quantity(reservationOrderDetail.getQuantity())
                    .foods(reservationOrderDetail.getFoods() != null ? foodMapper.converEntoResponse(reservationOrderDetail.getFoods()) : null)
                    .build();
        }

        public ReservationOrderDetail convertReqToEn(ReservationOrderDetailRequest reservationOrderDetailRequest){
            return ReservationOrderDetail.builder()
                    .price(reservationOrderDetailRequest.getPrice())
                    .quantity(reservationOrderDetailRequest.getQuantity())
                    .foods(reservationOrderDetailRequest.getFoodId() != null ? foodsDao.findById(reservationOrderDetailRequest.getFoodId())
                            .orElseThrow(() -> new EntityNotFoundException("Not found Food")) : null)
                    .reservationOrder(reservationOrderDetailRequest.getReservationOrderId() != null ? reservationOrderRepo.findById(reservationOrderDetailRequest.getReservationOrderId())
                            .orElseThrow(() -> new EntityNotFoundException("Not found ReservationOrder")) : null)
                    .build();
        }
    }

