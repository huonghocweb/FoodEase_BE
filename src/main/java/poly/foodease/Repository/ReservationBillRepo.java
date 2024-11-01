package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.ReservationBill;

public interface ReservationBillRepo extends JpaRepository<ReservationBill, Integer> {
}
