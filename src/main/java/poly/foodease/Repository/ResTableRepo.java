package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.ResTable;

public interface ResTableRepo extends JpaRepository<ResTable, Integer> {
}
