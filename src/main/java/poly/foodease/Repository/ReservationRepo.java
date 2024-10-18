package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.foodease.Model.Entity.Reservation;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
}
