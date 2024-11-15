package poly.foodease.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Response.ResTableResponse;
import poly.foodease.Model.Response.ReservationResponse;

public interface ResTableService {

    ResTableResponse createResTable(ResTableRequest resTableRequest);

    // ResTableResponse updateResTable(Integer tableId, ResTableRequest
    // resTableRequest);

    Optional<ResTableResponse> updateResTableNew(Integer tableId, ResTableRequest resTableRequest);

    // ResTableResponse getResTableById(Integer tableId);

    Optional<ResTableResponse> getResTableByIdNew(Integer tableId);

    void deleteResTable(Integer tableId);

    List<ResTableResponse> getAllResTables();

    Page<ResTableResponse> getAllResTable(Pageable pageable);
    Page<ResTableResponse> getResTableByTableCategory(Integer tableCategoryId, Pageable pageable);
    Page<ResTableResponse>  getResTableByCapacity(Integer capacity, Pageable pageable);
    Page<ResTableResponse>  getResTableByCapaAndCate(Integer tableCategoryId, Integer capacity, Pageable pageable);
    ReservationResponse checkResTableInReservation(Integer userId, Integer tableId, LocalDate checkinDate, LocalTime checkinTime, LocalTime checkoutTime, List<Integer> serviceIds) throws IOException, WriterException, MessagingException;
    Page<ResTableResponse> checkResTableByCapacityAndCheckinTime(Pageable pageable, Integer capacity);
}
