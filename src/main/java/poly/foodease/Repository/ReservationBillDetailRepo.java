package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.ReservationBillDetails;

public interface ReservationBillDetailRepo extends JpaRepository<ReservationBillDetails , Integer> {
}
