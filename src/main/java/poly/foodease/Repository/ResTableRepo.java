package poly.foodease.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.ResTable;

import java.time.LocalDateTime;
import java.util.List;

public interface ResTableRepo extends JpaRepository<ResTable, Integer> {

    @Query("SELECT rtb FROM ResTable rtb JOIN rtb.tableCategory tbCa WHERE tbCa.tableCategoryId = :tableCategoryId")
    Page<ResTable> getResTableByCategoryId(@Param("tableCategoryId") Integer tableCategoryId , Pageable pageable);

    @Query("SELECT rtb FROM ResTable rtb JOIN rtb.tableCategory tbCa WHERE tbCa.tableCategoryId = :tableCategoryId AND rtb.capacity >= :capacity")
    Page<ResTable> getResTableByCategoryIdAndCapacity(@Param("tableCategoryId") Integer tableCategoryId,
                                                            @Param("capacity") Integer capacity,
                                                            Pageable pageable);

    @Query("SELECT rtb FROM ResTable rtb JOIN rtb.tableCategory tbCa WHERE  rtb.capacity >= :capacity ")
    Page<ResTable> getResTableByCapacity(@Param("capacity") Integer capacity,
                                               Pageable pageable);

    @Query("SELECT rtb FROM ResTable rtb JOIN rtb.reservations res " +
            "WHERE rtb.tableId = :tableId " +
            "AND (:checkinTime BETWEEN res.checkinTime AND res.checkoutTime " +
            "OR :checkoutTime BETWEEN res.checkinTime AND res.checkoutTime " +
            "OR res.checkinTime BETWEEN :checkinTime AND :checkoutTime " +
            "OR res.checkoutTime BETWEEN :checkinTime AND :checkoutTime)")
    List<ResTable> checkResTableIsAvailable(@Param("tableId") Integer tableId,
                                         @Param("checkinTime") LocalDateTime checkinTime,
                                         @Param("checkoutTime") LocalDateTime checkoutTime);

    @Query("SELECT rtb FROM ResTable rtb " +
            "WHERE rtb.capacity >= :capacity " +
            "AND NOT EXISTS (" +
            "    SELECT res FROM rtb.reservations res " +
            "    WHERE :checkinTime BETWEEN res.checkinTime AND res.checkoutTime" +
            ")")
    Page<ResTable> checkResTableByCapacityAndCheckinTime(
            Pageable pageable,
            @Param("capacity") Integer capacity,
            @Param("checkinTime") LocalDateTime checkinTime);
}
