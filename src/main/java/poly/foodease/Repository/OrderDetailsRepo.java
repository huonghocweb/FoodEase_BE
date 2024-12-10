package poly.foodease.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Model.Entity.OrderDetails;
import poly.foodease.Model.Entity.OrderStatus;
import poly.foodease.Report.FoodBuyMost;

import java.util.List;

public interface OrderDetailsRepo extends JpaRepository<OrderDetails, Integer> {
    @Query("SELECT od FROM OrderDetails od JOIN od.order o WHERE o.orderId = :orderId")
    List<OrderDetails> getOrderDetailsByOrderId(@Param("orderId") Integer orderId);

    List<OrderDetails> findByOrder(Order order);

	    @Query("SELECT new poly.foodease.Report.FoodBuyMost(count(od.foodVariations.foodId) as countFood,od.foodVariations.food)"
	    		+ " from OrderDetails od group by od.foodVariations.foodId ,od.foodVariations.food "
	    		)  
	    Page<FoodBuyMost> FoodBuyMost(Pageable page);
	    
	    @Query("SELECT new poly.foodease.Report.FoodBuyMost(count(od.foodVariations.foodId) as countFood,od.foodVariations.food)"
	    		+ " from OrderDetails od where od.foodVariations.food.foodId = ?1 group by od.foodVariations.foodId ,od.foodVariations.food "
	    		)  
	    FoodBuyMost FoodSold(Integer foodId);
	    
	    @Query("SELECT new poly.foodease.Report.FoodSold(sum(od.quantity),od.foodVariations.foodVariationId)"
	    		+ "from OrderDetails od where od.foodVariations.foodVariationId = ?1 group by od.foodVariations.foodVariationId")
	    poly.foodease.Report.FoodSold foodSoldByFoodVariation(Integer foodVariationsId);
	    
	    @Query("SELECT new poly.foodease.Report.FoodSold(sum(quantity),od.foodVariations.food.foodId)"
	    		+ "from   OrderDetails od where od.foodVariations.food.foodId =?1 group by od.foodVariations.food.foodId")
	    poly.foodease.Report.FoodSold foodSoldByfoodId(Integer id);
}


