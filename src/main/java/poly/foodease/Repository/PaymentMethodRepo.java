package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.PaymentMethod;

public interface PaymentMethodRepo extends JpaRepository<PaymentMethod , Integer > {
}
