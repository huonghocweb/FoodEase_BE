package poly.foodease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.foodease.Model.Entity.Toppings;


public interface ToppingDao extends JpaRepository<Toppings, Integer>{

}
