package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.ReservationStatus;

public interface ReservationStatusRepo extends JpaRepository<ReservationStatus , Integer> {
}
