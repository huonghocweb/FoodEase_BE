package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.ReservationOrderDetail;

public interface ReservationOrderDetailRepo extends JpaRepository<ReservationOrderDetail, Integer> {
    @Query("SELECT resOD FROM ReservationOrderDetail resOD JOIN resOD.foods f " +
            " WHERE f.foodId = :foodId")
    ReservationOrderDetail getReservationOrderDetailByFoodId(@Param("foodId") Integer foodId);
}
