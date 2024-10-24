package poly.foodease.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.ResTableMapper;
import poly.foodease.Model.Entity.ResTable;
import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Model.Response.ResTableResponse;
import poly.foodease.Model.Response.ReservationResponse;
import poly.foodease.Repository.ResTableRepo;
import poly.foodease.Repository.ReservationRepo;
import poly.foodease.Service.ResTableService;
import poly.foodease.Service.ReservationService;

@Service
public class ResTableServiceImpl implements ResTableService {

    @Autowired
    private ResTableRepo resTableRepo;

    @Autowired
    private ResTableMapper resTableMapper;
    @Autowired
    private ReservationService reservationService;

    @Override
    public ResTableResponse createResTable(ResTableRequest resTableRequest) {
        ResTable newTable = resTableMapper.convertReqToEn(resTableRequest);
        resTableRepo.save(newTable);
        return resTableMapper.convertEnToRes(newTable);
    }


    @Override
    public Optional<ResTableResponse> updateResTableNew(Integer tableId, ResTableRequest resTableRequest) {
        return Optional.of(resTableRepo.findById(tableId)
                .map(existingTable -> {
                    ResTable resTable = resTableMapper.convertReqToEn(resTableRequest);
                    resTable.setTableId(existingTable.getTableId());
                    ResTable updatedTable = resTableRepo.save(resTable);
                    return resTableMapper.convertEnToRes(updatedTable);
                })
                .orElseThrow(() -> new EntityNotFoundException("not found Coupon")));
    }
    
    // @Override
    // public ResTableResponse updateResTable(Integer tableId, ResTableRequest resTableRequest) {
    //     ResTable existingTable = resTableRepo.findById(tableId)
    //             .orElseThrow(() -> new EntityNotFoundException("Table not found"));
    //     ResTable updatedTable = resTableMapper.convertReqToEn(resTableRequest);
    //     updatedTable.setTableId(existingTable.getTableId());
    //     resTableRepo.save(updatedTable);
    //     return resTableMapper.convertEnToRes(updatedTable);
    // }

    // @Override
    // public ResTableResponse getResTableById(Integer resTableId) {
    // ResTable resTable = resTableRepo.findById(resTableId)
    // .orElseThrow(() -> new EntityNotFoundException("Table not found"));
    // return resTableMapper.convertEnToRes(resTable);
    // }

    @Override
    public Optional<ResTableResponse> getResTableByIdNew(Integer resTableId) {
        ResTable resTable = resTableRepo.findById(resTableId)
                .orElseThrow(() -> new EntityNotFoundException("Not found ResTable"));
        return Optional.of(resTableMapper.convertEnToRes(resTable));
    }

    @Override
    public void deleteResTable(Integer tableId) {
        ResTable resTable = resTableRepo.findById(tableId)
                .orElseThrow(() -> new EntityNotFoundException("Table not found"));
        resTableRepo.delete(resTable);
    }

    @Override
    public List<ResTableResponse> getAllResTables() {
        return resTableRepo.findAll().stream()
                .map(resTableMapper::convertEnToRes)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ResTableResponse> getAllResTable(Pageable pageable) {
        Page<ResTable> resTablesPage = resTableRepo.findAll(pageable);
        List<ResTableResponse> resTables = resTablesPage.getContent().stream()
                .map(resTableMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(resTables, pageable , resTablesPage.getTotalElements());
    }


    // Huong
    @Override
    public Page<ResTableResponse> getResTableByTableCategory(Integer tableCategoryId , Pageable pageable) {
        Page<ResTable> resTablesPage = resTableRepo.getResTableByCategoryId(tableCategoryId , pageable );
        List<ResTableResponse> resTables = resTablesPage.getContent().stream()
                .map(resTableMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(resTables, pageable , resTablesPage.getTotalElements());
    }

    @Override
    public Page<ResTableResponse>  getResTableByCapacity(Integer capacity, Pageable pageable) {
        Page<ResTable> resTablesPage = resTableRepo.getResTableByCapacity(capacity , pageable );
        List<ResTableResponse> resTables = resTablesPage.getContent().stream()
                .map(resTableMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(resTables, pageable , resTablesPage.getTotalElements());
    }

    @Override
    public Page<ResTableResponse>  getResTableByCapaAndCate(Integer tableCategoryId, Integer capacity, Pageable pageable) {
        Page<ResTable> resTablesPage = resTableRepo.getResTableByCategoryIdAndCapacity(tableCategoryId ,capacity , pageable );
        List<ResTableResponse> resTables = resTablesPage.getContent().stream()
                .map(resTableMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(resTables, pageable , resTablesPage.getTotalElements());
    }

    @Override
    public ReservationResponse checkResTableInReservation(Integer userId, Integer tableId, LocalDate checkinDate, LocalTime checkinTime, LocalTime checkoutTime){
        ResTableResponse resTable = this.getResTableByIdNew(tableId)
                .orElseThrow(() -> new EntityNotFoundException("N"));
        LocalDateTime checkin = checkinDate.atTime(checkinTime);
        LocalDateTime checkout = checkinDate.atTime(checkoutTime);
        List<ResTable> resTables = resTableRepo.checkResTableIsAvailable(tableId,checkin,checkout);
        System.out.println("123");
        System.out.println("ResTable" + resTable);
        if (resTables.isEmpty()){
            ReservationRequest reservationRequest = new ReservationRequest();
            reservationRequest.setResTableIds(tableId);
            reservationRequest.setUserId(userId);
            reservationRequest.setCheckinTime(checkin);
            reservationRequest.setCheckoutTime(checkout);
            reservationRequest.setGuests(1);
            reservationRequest.setTotalDeposit(resTable.getDeposit());
            reservationRequest.setReservationStatusId(1);
            System.out.println(reservationRequest);
            System.out.println("Create Reservation");
            return reservationService.createReservation(reservationRequest);
        }else {
            System.out.println("failed");

            return null;

        }
    }
}
