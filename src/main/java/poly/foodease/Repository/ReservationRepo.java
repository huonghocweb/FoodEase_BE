package poly.foodease.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
    @Query("SELECT res FROM Reservation res JOIN res.user u WHERE u.userName = :userName")
    Page<Reservation> getReservationByReservationByUserName(@Param("userName") String userName, Pageable pageable);

    @Query("SELECT res FROM Reservation res JOIN res.resTable rtb JOIN res.reservationStatus resSta " +
            "WHERE rtb.tableId = :tableId  AND resSta.reservationStatusId = 1 " +
            "AND res.checkinTime >= :startOfDay AND res.checkinTime < :endOfDay " )
    List<Reservation> getReservationsByTableIdAndDate(
            @Param("tableId") Integer tableId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);


    @Query("SELECT res FROM Reservation res WHERE res.bookTime BETWEEN :startDateTime AND :endDateTime")
    Page<Reservation> getReservationFilterByBookDate(Pageable pageable,
                                                     @Param("startDateTime") LocalDateTime startDateTime,
                                                     @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT res FROM Reservation res JOIN res.services ser" +
            " JOIN res.user u JOIN res.resTable tbl" +
            " WHERE u.fullName LIKE CONCAT('%' , :keyWord , '%')" +
            " OR tbl.tableName LIKE CONCAT('%' , :keyWord ,'%') " +
            " OR ser.serviceName LIKE CONCAT('%', :keyWord ,'%')")
    Page<Reservation> getReservationByKeyWord(@Param("keyWord") String keyWord,
                                              Pageable pageable);

    @Query("SELECT res FROM Reservation res JOIN res.reservationStatus resSta WHERE resSta.reservationStatusId = :reservationStatusId")
    List<Reservation> getReservationByReservationStatusId(@Param("reservationStatusId") Integer reservationStatusId );
}
