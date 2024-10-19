package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.TableCategory;

public interface TableCategoryRepo extends JpaRepository<TableCategory, Integer> {
}
