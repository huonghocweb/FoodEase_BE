package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.Role;

public interface RoleRepo extends JpaRepository<Role,Integer> {
}
