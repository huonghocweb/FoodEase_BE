package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.TableServices;

public interface TableServiceRepo extends JpaRepository<TableServices ,Integer> {
}
