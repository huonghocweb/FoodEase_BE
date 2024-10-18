package poly.foodease.Repository;

import java.util.List; // Sử dụng java.util.List

import org.springframework.data.jpa.repository.JpaRepository;

import poly.foodease.Model.Entity.ResTable;

public interface ResTableRepo extends JpaRepository<ResTable, Integer> {
    List<ResTable> findAllByIsAvailable(boolean isAvailable);

    List<ResTable> findByIsAvailableTrue(); // Định nghĩa phương thức

    List<ResTable> findByCapacityBetween(int minCapacity, int maxCapacity);

    Boolean existsByTableName(String name);

}
