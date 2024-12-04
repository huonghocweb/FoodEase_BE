package poly.foodease.Controller.Api;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Model.Entity.OrderDetails;
import poly.foodease.Model.Entity.Toppings;
import poly.foodease.Model.Response.*;
import poly.foodease.Model.Response.OrderDTO;
import poly.foodease.Model.Response.OrderDetailDTO;
import poly.foodease.Model.Response.OrderResponse;
import poly.foodease.Report.ReportOrder;
import poly.foodease.Report.ReportRevenueByMonth;
import poly.foodease.Report.ReportRevenueByYear;
import poly.foodease.Report.ReportUserBuy;
import poly.foodease.Service.OrderDetailsService;
import poly.foodease.Service.OrderService;
import poly.foodease.Utils.UserBuyExportExcel;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/order")
public class OrderApi {

	@Autowired
	OrderService orderService;

	@Autowired
	private DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

	@Autowired
	OrderDetailsService orderDetailsService;

	@GetMapping("/orderHistory/{userName}")
	public ResponseEntity<Object> getOrdersByUserName(@PathVariable("userName") String userName,
			@RequestParam("pageCurrent") Integer pageCurrent, @RequestParam("pageSize") Integer pageSize,
			@RequestParam("sortOrder") String sortOrder, @RequestParam("sortBy") String sortBy) {
		Map<String, Object> result = new HashMap<>();
		try {
			result.put("success", true);
			result.put("message", "Get Orders by userName");

		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
			result.put("data", null);
		}
		return ResponseEntity.ok(result);
	}

	@GetMapping("/orderHistoryByOrderStatus/{userName}")
	public ResponseEntity<Object> getOrderByOrderStatus(@PathVariable("userName") String userName,
			@RequestParam("pageCurrent") Integer pageCurrent, @RequestParam("pageSize") Integer pageSize,
			@RequestParam("sortOrder") String sortOrder,
			@RequestParam(value = "orderStatusId", required = false) Integer orderStatusId,
			@RequestParam("sortBy") String sortBy) {
		Map<String, Object> result = new HashMap<>();
		System.out.println("Get get Order By OrderStatus" + orderStatusId);
		try {
			result.put("success", true);
			result.put("message", "Get Orders by Status Id");
			if (orderStatusId != 0) {
				result.put("data", orderService.getOrderByStatusId(userName, orderStatusId, pageCurrent, pageSize,
						sortOrder, sortBy));
			} else {
				result.put("data", orderService.getOrderByUserName(userName, pageCurrent, pageSize, sortOrder, sortBy));
			}

		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
			result.put("data", null);
		}
		return ResponseEntity.ok(result);
	}

	@GetMapping("/changeOrderStatus/{orderId}/{orderStatusId}")
	public ResponseEntity<Object> changeOrderStatus(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderStatusId") Integer orderStatusId) {
		Map<String, Object> result = new HashMap<>();
		System.out.println("Change Order Status to " + orderStatusId);
		try {
			result.put("success", true);
			result.put("message", "Change Order Status By orderStatusId");
			result.put("data", orderService.changeOrderStatus(orderId, orderStatusId));
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
			result.put("data", null);
		}
		return ResponseEntity.ok(result);
	}

	// Ngọc

	@GetMapping("/findAll")
	public ResponseEntity<List<OrderResponse>> findAll() {
		List<OrderResponse> list = orderService.findAll();
		return ResponseEntity.ok(list);
	}

//<<<<<<< HEAD
//	@GetMapping("/findTotalPriceAndQuantityByOrderDate")
//	public ResponseEntity<List<ReportOrder>> findTotalPriceAndQuantityByOrderDate() {
//		try {
//			List<ReportOrder> list = orderService.findTotalPriceAndQuantityByOrderDate();
//=======

    @GetMapping("/findTotalPriceAndQuantityByOrderDate")
    public ResponseEntity<List<ReportOrder>> findTotalPriceAndQuantityByOrderDate()
    {
        try {
            List<ReportOrder> list=orderService.findTotalPriceAndQuantityByOrderDate();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }

    }
    @GetMapping("/ReportRevenueByMonth")
    public ResponseEntity<List<ReportRevenueByMonth>> ReportRevenueByMonth()
    {
        try {
            List<ReportRevenueByMonth> list=orderService.getRevenueByMonth();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }

    }
    @GetMapping("/ReportRevenueByYear")
    public ResponseEntity<List<ReportRevenueByYear>> ReportRevenueByYear(){
        try {
            List<ReportRevenueByYear> list=orderService.ReportRevenueByYear();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }


    }
    @GetMapping("/ReportUserBuy")
    public ResponseEntity<Page<ReportUserBuy>> ReportUserBuy(@RequestParam("date") Optional<LocalDate> date,
    		@RequestParam("page") Optional<Integer> page){
        try {
            Pageable pageable = PageRequest.of(page.orElse(0), 3);
            LocalDate dateToday=date.orElse(LocalDate.now());
            Page<ReportUserBuy> list=orderService.findReportUserBuy(dateToday,pageable);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }

    }

    @GetMapping("/{orderId}/details")
    public ResponseEntity<OrderDTO> getOrderWithDetails(@PathVariable Integer orderId) {
        // Lấy Order entity từ service
        Order order = orderService.getOrderById(orderId);

        // Kiểm tra xem đơn hàng có tồn tại không
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        // Lấy danh sách OrderDetails entity
        List<OrderDetails> orderDetailsList = orderDetailsService.findByOrderId(orderId);

        // Tạo OrderResponse
        OrderDTO orderResponse = new OrderDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(order.getUser().getFullName());
        userDTO.setEmail(order.getUser().getEmail());
        userDTO.setPhoneNumber(order.getUser().getPhoneNumber());
        orderResponse.setUser(userDTO);
        orderResponse.setOrderDate(order.getOrderDate().toString());
        orderResponse.setDeliveryAddress(order.getDeleveryAddress());

        // Chuyển đổi OrderDetails sang OrderDetailResponse
        List<OrderDetailDTO> details = orderDetailsList.stream().map(detail -> {
            OrderDetailDTO detailResponse = new OrderDetailDTO();
            detailResponse.setFoodName(detail.getFoodVariations().getFood().getFoodName());
            detailResponse.setPrice(detail.getPrice());
            detailResponse.setQuantity(detail.getQuantity());
            detailResponse.setSize(detail.getFoodVariations().getFoodSize().getFoodSizeName());

            List<String> toppingNames = detail.getFoodVariations().getFoodVariationToppings().stream()
                    .map(foodVariationTopping -> foodVariationTopping.getToppings().getToppingName())
                    .collect(Collectors.toList());

            detailResponse.setTopping(toppingNames);
            return detailResponse;
        }).collect(Collectors.toList());

        orderResponse.setOrderDetails(details);

        // Trả về OrderResponse
        return ResponseEntity.ok(orderResponse);
    }





//	@GetMapping("/{orderId}/details")
//	public ResponseEntity<OrderDTO> getOrderWithDetails(@PathVariable Integer orderId) {
//		// Lấy Order entity từ service
//		Order order = orderService.getOrderById(orderId);
//
//		// Kiểm tra xem đơn hàng có tồn tại không
//		if (order == null) {
//			return ResponseEntity.notFound().build();
//		}
//
//		// Lấy danh sách OrderDetails entity
//		List<OrderDetails> orderDetailsList = orderDetailsService.findByOrderId(orderId);
//
//		// Tạo OrderResponse
//		OrderDTO orderResponse = new OrderDTO();
//		orderResponse.setUser(order.getUser().getFullName()); // Lấy tên người dùng từ Order
//		orderResponse.setOrderDate(order.getOrderDate().toString()); // Chuyển đổi ngày
//		orderResponse.setDeliveryAddress(order.getDeleveryAddress());
//
//		// Chuyển đổi OrderDetails sang OrderDetailResponse
//		List<OrderDetailDTO> details = orderDetailsList.stream().map(detail -> {
//			OrderDetailDTO detailResponse = new OrderDetailDTO();
//			detailResponse.setFoodName(detail.getFoodVariations().getFood().getFoodName());
//			detailResponse.setPrice(detail.getPrice());
//			detailResponse.setQuantity(detail.getQuantity());
//			return detailResponse;
//		}).collect(Collectors.toList());
//
//		orderResponse.setOrderDetails(details);
//
//		// Trả về OrderResponse
//		return ResponseEntity.ok(orderResponse);
//	}

	@GetMapping("/ReportOrderByToday")
	public ResponseEntity<Page<ReportOrder>> ReportOrderByToday(@RequestParam("date") Optional<LocalDate> date,
			@RequestParam("page") Optional<Integer> page) {
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), 3);
			LocalDate dateToDay = date.orElse(LocalDate.now());
			Page<ReportOrder> list = orderService.ReportRevenueByToday(dateToDay, pageable);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	@GetMapping("/findOrderByOrderDate")
	public ResponseEntity<Page<OrderResponse>> findOrderByOrderDate(@RequestParam("date") Optional<LocalDate> date,
			@RequestParam("page") Optional<Integer> page, @RequestParam("EndDate") Optional<LocalDate> endDate,
			 @RequestParam(value = "sortBy", required = false, defaultValue = "orderDate") String sortBy,
		        @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") String sortDirection) {
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), 5,Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
			if (!date.isPresent() && !endDate.isPresent()) {
				LocalDate today = date.orElse(LocalDate.now());

				Page<OrderResponse> list = orderService.findOrderByOrderDate(today, pageable);
				return ResponseEntity.ok(list);
			}

			else if (date != null && endDate != null) {

				Page<OrderResponse> beetWeenDate = orderService.findOrdersByOrderDateBetween(date, endDate, pageable);
				return ResponseEntity.ok(beetWeenDate);
			}
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	@GetMapping("/exportUserBuy")
	public void exportUserBuy(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);
		List<ReportUserBuy> list = orderService.findAllReportUserBuy();
		UserBuyExportExcel excelExporter = new UserBuyExportExcel(list);

		excelExporter.export(response);

	}

	@GetMapping("/byId/{orderId}")
	public ResponseEntity<Object> getOrderById(
			@PathVariable("orderId") Integer orderId
	){
		Map<String,Object> result = new HashMap<>();
		try {
			result.put("success",true);
			result.put("message","get Order By Order Id");
			result.put("data",orderService.getOrderByOrderId(orderId));
		}catch (Exception e){
			result.put("success",false);
			result.put("message",e.getMessage());
			result.put("data", null);
		}
		return ResponseEntity.ok(result);
	}

}
