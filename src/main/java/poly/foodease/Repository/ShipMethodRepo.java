package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.ShipMethod;

public interface ShipMethodRepo extends JpaRepository<ShipMethod,Integer> {
}
