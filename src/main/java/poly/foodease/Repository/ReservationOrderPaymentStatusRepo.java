package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.ReservationOrderPaymentStatus;

public interface ReservationOrderPaymentStatusRepo extends JpaRepository<ReservationOrderPaymentStatus , Integer> {
}
