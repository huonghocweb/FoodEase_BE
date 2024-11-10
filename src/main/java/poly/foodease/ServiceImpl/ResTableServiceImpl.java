package poly.foodease.ServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.ResTableMapper;
import poly.foodease.Model.Entity.MailInfo;
import poly.foodease.Model.Entity.ResTable;
import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Request.ReservationRequest;
import poly.foodease.Model.Response.ResTableResponse;
import poly.foodease.Model.Response.ReservationResponse;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Repository.ResTableRepo;
import poly.foodease.Repository.ReservationRepo;
import poly.foodease.Service.*;

@Service
public class ResTableServiceImpl implements ResTableService {

    @Autowired
    private ResTableRepo resTableRepo;

    @Autowired
    private ResTableMapper resTableMapper;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;
    @Autowired
    private MailInfo mailInfo;
    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResTableResponse createResTable(ResTableRequest resTableRequest) {
        ResTable newTable = resTableMapper.convertReqToEn(resTableRequest);
        resTableRepo.save(newTable);
        return resTableMapper.convertEnToRes(newTable);
    }


    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STAFF')")
    @Override
    public Page<ResTableResponse> getResTableByTableCategory(Integer tableCategoryId , Pageable pageable) {
        Page<ResTable> resTablesPage = resTableRepo.getResTableByCategoryId(tableCategoryId , pageable );
        List<ResTableResponse> resTables = resTablesPage.getContent().stream()
                .map(resTableMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(resTables, pageable , resTablesPage.getTotalElements());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STAFF')")
    @Override
    public Page<ResTableResponse>  getResTableByCapacity(Integer capacity, Pageable pageable) {
        Page<ResTable> resTablesPage = resTableRepo.getResTableByCapacity(capacity , pageable );
        List<ResTableResponse> resTables = resTablesPage.getContent().stream()
                .map(resTableMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(resTables, pageable , resTablesPage.getTotalElements());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STAFF')")
    @Override
    public Page<ResTableResponse>  getResTableByCapaAndCate(Integer tableCategoryId, Integer capacity, Pageable pageable) {
        Page<ResTable> resTablesPage = resTableRepo.getResTableByCategoryIdAndCapacity(tableCategoryId ,capacity , pageable );
        List<ResTableResponse> resTables = resTablesPage.getContent().stream()
                .map(resTableMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(resTables, pageable , resTablesPage.getTotalElements());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STAFF')")
    @Override
    public ReservationResponse checkResTableInReservation(Integer userId, Integer tableId, LocalDate checkinDate, LocalTime checkinTime, LocalTime checkoutTime, List<Integer> serviceIds) throws IOException, WriterException, MessagingException {
        ResTableResponse resTable = this.getResTableByIdNew(tableId)
                .orElseThrow(() -> new EntityNotFoundException("Not Found ResTable"));
        UserResponse user = userService.getUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Not found User"));
        LocalDateTime checkin = checkinDate.atTime(checkinTime);
        LocalDateTime checkout = checkinDate.atTime(checkoutTime);
        List<ResTable> resTables = resTableRepo.checkResTableIsAvailable(tableId,checkin,checkout);
        System.out.println("123");
        System.out.println("ResTable" + resTable);
        if (resTables.isEmpty()){
            ReservationRequest reservationRequest = new ReservationRequest();
            String randomCode = "CK" + UUID.randomUUID().toString().substring(0,5);
            reservationRequest.setCheckinCode(passwordEncoder.encode(randomCode));
            System.out.println(reservationRequest.getCheckinCode());
            reservationRequest.setResTableIds(tableId);
            reservationRequest.setUserId(userId);
            reservationRequest.setCheckinTime(checkin);
            reservationRequest.setCheckoutTime(checkout);
            reservationRequest.setGuests(1);
            reservationRequest.setTotalDeposit(resTable.getDeposit());
            reservationRequest.setReservationStatusId(1);
            reservationRequest.setServiceIds(serviceIds);
            System.out.println(reservationRequest);
            System.out.println("Create Reservation");
            ReservationResponse reservationCreate = reservationService.createReservation(reservationRequest);
            if(reservationCreate != null){
                mailInfo.setTo(user.getEmail());
                StringBuilder bodyBuilder = new StringBuilder();
                bodyBuilder.append("<html><body>");
                bodyBuilder.append("<p>Kính chào quý khách,</p>");
                bodyBuilder.append("<p>Cảm ơn quý khách đã đặt bàn tại Victory Restaurant. Dưới đây là thông tin đặt bàn của quý khách:</p>");
                bodyBuilder.append("<p><strong>Tên khách hàng:</strong> ").append(user.getFullName()).append("<br>");
                bodyBuilder.append("<strong>Email:</strong> ").append(user.getEmail()).append("<br>");
                bodyBuilder.append("<strong>Số điện thoại:</strong> ").append(user.getPhoneNumber()).append("<br>");
                bodyBuilder.append("<p>Thông tin chi tiết:</p>");
                bodyBuilder.append("<ul>");
                bodyBuilder.append("<strong>Mã bàn:</strong> ").append(reservationCreate.getResTable().getTableId()).append("<br>");
                bodyBuilder.append("<strong>Ngày Check In : </strong> ").append(reservationCreate.getCheckinTime().toLocalDate()).append("<br>");
                bodyBuilder.append("<strong>Giờ Check In:</strong> ").append(reservationCreate.getCheckinTime().toLocalTime()).append("<br>");
                bodyBuilder.append("<strong>Giờ Check Out:</strong> ").append(reservationCreate.getCheckoutTime().toLocalTime()).append("<br>");
                bodyBuilder.append("<strong>Tổng tiền cọc:</strong> ").append(reservationCreate.getTotalDeposit()).append(" VND</p>");
                bodyBuilder.append("</ul>");
                bodyBuilder.append("<p>Quý khách có thể quét Mã QR để xem thông tin mã checkin .</p>");
                bodyBuilder.append("<p>Chúc quý khách có một ngày vui vẻ!</p>");
                bodyBuilder.append("<p>Trân trọng,<br>Công ty Victory Group</p>");
                bodyBuilder.append("</body></html>");
                List<File> files = new ArrayList<>();
                File qrcodeFile = qrCodeService.createQrCodeWithFileTemp("Mã Checkin Của Bạn Là  : " + randomCode , 360, 360);
                mailInfo.setBody(bodyBuilder.toString());
                mailInfo.setSubject("Reservation Information");
                files.add(qrcodeFile);
                mailInfo.setFiles(files);
                mailService.send(mailInfo);
            }
            return reservationCreate;
        }else {
            System.out.println("failed");
            return null;

        }
    }
}
