package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.ReservationOrderDetailMapper;
import poly.foodease.Mapper.ReservationOrderMapper;
import poly.foodease.Model.Entity.ReservationOrder;
import poly.foodease.Model.Response.ReservationOrderResponse;
import poly.foodease.Repository.ResTableRepo;
import poly.foodease.Repository.ReservationOrderDetailRepo;
import poly.foodease.Repository.ReservationOrderRepo;
import poly.foodease.Repository.ReservationRepo;
import poly.foodease.Service.ReservationOrderService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationOrderServiceImpl implements ReservationOrderService {

    @Autowired
    ReservationOrderRepo reservationOrderRepo;
    @Autowired
    private ReservationOrderMapper reservationOrderMapper;
    @Autowired
    private ReservationOrderDetailRepo reservationOrderDetailRepo;
    @Autowired
    private ReservationOrderDetailMapper reservationOrderDetailMapper;
    @Autowired
    private ResTableRepo resTableRepo ;
    @Autowired
    private ReservationRepo reservationRepo;


    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Override
    public Page<ReservationOrderResponse> getAllReservationOrder(Pageable pageable) {
        Page<ReservationOrder> reservationOrderPage = reservationOrderRepo.findAll(pageable);
        List<ReservationOrderResponse> reservationOrderResponses = reservationOrderPage.getContent().stream()
                .map(reservationOrderMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(reservationOrderResponses,pageable , reservationOrderPage.getTotalElements());
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    @Override
    public ReservationOrderResponse getReservationOrderById(Integer reservationOrderId) {
        ReservationOrder reservationOrder = reservationOrderRepo.findById(reservationOrderId)
                .orElseThrow(() -> new EntityNotFoundException("not found ReservationOrder"));
        return reservationOrderMapper.convertEnToRes(reservationOrder);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    @Override
    public ReservationOrderResponse getReservationOrderByReservationId(Integer reservationId) {
        ReservationOrder reservationOrder = reservationOrderRepo.getReservationOrderByReservationId(reservationId);
        return reservationOrderMapper.convertEnToRes(reservationOrder);
    }

    @Override
    public ReservationOrderResponse changeTableInReservationOrder(Integer reservationOrderId, Integer resTableId){
        ReservationOrder reservationOrder = reservationOrderRepo.findById(reservationOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Not found Reservation Order"));
        reservationOrder.getReservation().setResTable(resTableRepo.findById(resTableId)
                .orElseThrow(() -> new EntityNotFoundException("not Found ResTable")));
        ReservationOrder reservationOrderUpdated = reservationOrderRepo.save(reservationOrder);
        return reservationOrderMapper.convertEnToRes(reservationOrderUpdated);
    }
}
