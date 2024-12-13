	package poly.foodease.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Model.Response.PaymentMethodRevenueResponse;
import poly.foodease.Report.ReportOrder;
import poly.foodease.Report.ReportRevenueByDay;
import poly.foodease.Report.ReportRevenueByMonth;
import poly.foodease.Report.ReportRevenueByYear;
import poly.foodease.Report.ReportUserBuy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order ,Integer> {

    @Query("SELECT o FROM Order o JOIN o.user u WHERE u.userName LIKE  :userName")
    Page<Order> getOrdersByUserName(@Param("userName") String userName, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN o.orderStatus ost JOIN o.user u WHERE ost.orderStatusId = :orderStatusId AND u.userName=:userName")
    Page<Order> getOrderByStatusId(@Param("userName") String userName , @Param("orderStatusId") Integer orderStatusId , Pageable pageable);

    @Query("SELECT o FROM Order o JOIN o.orderStatus ost WHERE ost.orderStatusId = :orderStatusId")
    List<Order> getOrderIsShipping(@RequestParam("orderStatusId") Integer orderStatusId);

    @Query("SELECT o FROM Order o JOIN o.orderStatus ost WHERE ost.orderStatusId IN :orderStatusIds")
    List<Order> getOrdersToUpdate(@Param("orderStatusIds") List<Integer> orderStatusIds);

    // Ngọc
    @Query("SELECT new poly.foodease.Report.ReportOrder(o.orderDate, o.orderTime, SUM(o.totalPrice), SUM(o.totalQuantity)) " +
            "FROM Order o GROUP BY o.orderDate, o.orderTime ORDER BY o.orderDate")
    List<ReportOrder> findTotalPriceAndQuantityByOrderDate();

    @Query("SELECT new poly.foodease.Report.ReportRevenueByMonth(YEAR(o.orderDate) AS year, MONTH(o.orderDate) AS month, SUM(o.totalPrice) AS totalPrice, SUM(o.totalQuantity) AS totalQuantity) " +
            "FROM Order o " +
            "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) " +
            "ORDER BY year, month")
    List<ReportRevenueByMonth> getRevenueByMonth();

    @Query("SELECT new poly.foodease.Report.ReportRevenueByYear(YEAR(o.orderDate) AS year, SUM(o.totalPrice), SUM(o.totalQuantity))"
            + "FROM Order o GROUP BY YEAR(o.orderDate) ORDER BY year")
    List<ReportRevenueByYear> ReportRevenueByYear();

    @Query("SELECT new poly.foodease.Report.ReportUserBuy(o.user.userId,o.orderDate,o.user.fullName,o.user.gender,o.user.phoneNumber,o.user.address,o.user.birthday,o.user.email,SUM(o.totalQuantity),SUM(o.totalPrice))"
            + " FROM Order o WHERE o.orderDate =:date GROUP BY o.user.userId,o.orderDate,o.user.fullName,o.user.gender,o.user.phoneNumber,o.user.address,o.user.birthday,o.user.email")
    Page<ReportUserBuy> findReportUserBuy(@Param("date") LocalDate date,Pageable page);
    
    @Query("SELECT o FROM Order o where o.orderDate = :date")
    Page<Order> findOrderByOrderDate(@Param("date") LocalDate date,Pageable page);
    
    @Query("SELECT new poly.foodease.Report.ReportOrder(o.orderDate, o.orderTime, SUM(o.totalPrice), SUM(o.totalQuantity)) " +
            "FROM Order o WHERE o.orderDate = :date GROUP BY o.orderDate, o.orderTime ORDER BY o.orderDate")
    Page<ReportOrder> ReportRevenueByToday(LocalDate date,Pageable page);


    @Query("SELECT new poly.foodease.Model.Response.PaymentMethodRevenueResponse(pm.paymentName, SUM(o.totalPrice), COUNT(o.user.userId), COUNT(o.orderId)) " +
            "FROM Order o JOIN o.paymentMethod pm " +
            "WHERE (:year IS NULL OR FUNCTION('YEAR', o.orderDate) = :year) " +
            "AND (:month IS NULL OR FUNCTION('MONTH', o.orderDate) = :month) " +
            "AND (:startDate IS NULL OR o.orderDate >= :startDate) " +
            "AND (:endDate IS NULL OR o.orderDate <= :endDate) " +
            "GROUP BY pm.paymentName")
    List<PaymentMethodRevenueResponse> getRevenueByPaymentMethod(@Param("year") Integer year,
                                                                 @Param("month") Integer month,
                                                                 @Param("startDate") LocalDate startDate,
                                                                 @Param("endDate") LocalDate endDate);
    //ngọc
    @Query("SELECT new poly.foodease.Report.ReportUserBuy(o.user.userId,o.orderDate,o.user.fullName,o.user.gender,o.user.phoneNumber,o.user.address,o.user.birthday,o.user.email,SUM(o.totalQuantity),SUM(o.totalPrice))"
            + " FROM Order o  GROUP BY o.user.userId,o.orderDate,o.user.fullName,o.user.gender,o.user.phoneNumber,o.user.address,o.user.birthday,o.user.email")
    List<ReportUserBuy> findAllReportUserBuy();

    @Query("SELECT o FROM Order o  WHERE MONTH(o.orderDate) = :month AND YEAR(o.orderDate) = :year")
    List<Order> findByDate(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    Page<Order> findOrdersByOrderDateBetween(@Param("startDate") Optional<LocalDate> date, @Param("endDate") Optional<LocalDate> endDate, Pageable page);
    
    @Query("SELECT new poly.foodease.Report.ReportRevenueByDay(o.orderDate,sum(o.totalPrice))"
    		+ "from Order o where o.orderDate  BETWEEN :startDate AND :endDate group by o.orderDate")
    Page<ReportRevenueByDay> ReportRevenueByDay(@Param("startDate") Optional<LocalDate> date, @Param("endDate") Optional<LocalDate> endDate, Pageable page);
    
    @Query("SELECT new poly.foodease.Report.ReportRevenueByDay(o.orderDate,sum(o.totalPrice))"
    		+ "from Order o where o.orderDate =?1 group by o.orderDate")
    Page<ReportRevenueByDay> ReportRevenueDayByToday(LocalDate today,Pageable page);
    
    @Query("SELECT new poly.foodease.Report.ReportRevenueByMonth(YEAR(o.orderDate), MONTH(o.orderDate), SUM(o.totalPrice), SUM(o.totalQuantity)) " +
    	       "FROM Order o " +
    	       "WHERE YEAR(o.orderDate) = ?1 " +
    	       "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) " +
    	       "ORDER BY YEAR(o.orderDate), MONTH(o.orderDate)")
    	List<ReportRevenueByMonth> getRevenueByMonthAndYear(Integer year);
    
    
    @Query("SELECT new poly.foodease.Report.ReportRevenueByYear(YEAR(o.orderDate), SUM(o.totalPrice), SUM(o.totalQuantity)) " +
    	       "FROM Order o WHERE YEAR(o.orderDate) = ?1 " +
    	       "GROUP BY YEAR(o.orderDate) " +
    	       "ORDER BY YEAR(o.orderDate)")
    	List<ReportRevenueByYear> ReportRevenueByYear1(Integer year);

}
