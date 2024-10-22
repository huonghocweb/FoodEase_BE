package poly.foodease.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Response.ResTableResponse;

public interface ResTableService {

    ResTableResponse createResTable(ResTableRequest resTableRequest);

    ResTableResponse updateResTable(Integer tableId, ResTableRequest resTableRequest);

    ResTableResponse getResTableById(Integer tableId);

    void deleteResTable(Integer tableId);

    List<ResTableResponse> getAllResTables();

    Page<ResTableResponse> getAllResTable(Pageable pageable);
    Page<ResTableResponse> getResTableByTableCategory(Integer tableCategoryId, Pageable pageable);
    Page<ResTableResponse>  getResTableByCapacity(Integer capacity, Pageable pageable);
    Page<ResTableResponse>  getResTableByCapaAndCate(Integer tableCategoryId, Integer capacity, Pageable pageable);
    List<ResTableResponse> checkResTableInReservation(LocalDate checkinDate, LocalTime checkinTime, LocalTime checkoutTime);
}
