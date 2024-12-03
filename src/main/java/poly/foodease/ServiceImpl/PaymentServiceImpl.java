package poly.foodease.ServiceImpl;

import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.OrderMapper;
import poly.foodease.Mapper.PaymentMethodMapper;
import poly.foodease.Model.Entity.*;
import poly.foodease.Model.Request.OrderDetailsRequest;
import poly.foodease.Model.Request.OrderRequest;
import poly.foodease.Model.Response.*;
import poly.foodease.Repository.*;
import poly.foodease.Service.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private MailService mailService;
    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private PaymentMethodRepo paymentMethodRepo;
    @Autowired
    private PaymentMethodMapper paymentMethodMapper;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderStatusRepo orderStatusRepo;
    @Autowired
    private CouponStorageRepo couponStorageRepo;
    @Autowired
    private CouponStorageService couponStorageService;
    @Autowired
    private CouponRepo couponRepo;
    @Autowired
    private FoodVariationsDao foodVariationsDao;

    public PaymentInfo createPaymentInfo(String orderInfo, Integer paymentStatus, String totalPrice, String paymentDateTime, String transactionId){
        PaymentInfo paymentInfo = new PaymentInfo(orderInfo,paymentStatus,paymentDateTime,totalPrice,transactionId);
        System.out.println( "Payment Info " + paymentInfo);
        return paymentInfo;
    }
    // Lưu thông tin hóa đơn trước khi thực hiện thanh toán với status là processing
    public OrderResponse createOrder(Integer cartId, Integer couponId , Integer paymentMethodId, Integer shipMethodId ,Integer leadTime,Integer shipFee, String deliveryAddress){
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(cartId);
        orderRequest.setTotalPrice(cartService.getTotalPrice(cartId));
        orderRequest.setTotalQuantity(cartService.getTotalQuantity(cartId) );
        // Lưu thông tin thanh toán với trạng thái Processing
        orderRequest.setOrderStatusId(1);
        orderRequest.setPaymentMethodId(paymentMethodId);
        orderRequest.setShipMethodId(shipMethodId);
        orderRequest.setOrderDate(LocalDate.now());
        orderRequest.setOrderTime(LocalTime.now());
        orderRequest.setLeadTime(leadTime);
        orderRequest.setShipFee(shipFee);
        orderRequest.setDeliveryAddress(deliveryAddress);
        // Bổ sung leadTime, paymentDateTime và estimatedDeliveryDateTime
        if(couponId != null ){
            orderRequest.setCouponId(couponId);
        }
       // System.out.println( orderRequest);
        return orderService.createOrder(orderRequest);
    }

    // Cập nhật thông tin sau khi thanh toán thành công
    public OrderResponse updatePaymentSuccess(Integer orderId){
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Not found Order"));
        order.setPaymentDatetime(LocalDateTime.now());
        order.setOrderStatus(orderStatusRepo.findById(2)
                .orElseThrow(() -> new EntityNotFoundException("NOT found Order Status")));
        order.setEstimatedDeliveryDateTime(LocalDateTime.now().plusHours(order.getLeadTime()));
        return orderMapper.convertEnToRes(orderRepo.save(order));
    }
    public List<OrderDetailsResponse> createOrderDetails(Integer orderId, Integer cartId){
        OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest();
        List<OrderDetailsResponse> orderDetailsResponses = new ArrayList<>();
        Cart cart = cartService.getCart(cartId);
        for (Map.Entry<Integer , CartItem> cartItem : cart.getItems().entrySet()){
            orderDetailsRequest.setFoodVaId(cartItem.getKey());
            System.out.println("Food Va Id " + cartItem.getKey());
            orderDetailsRequest.setOrderId(orderId);
            orderDetailsRequest.setPrice(cartItem.getValue().getPrice());
            orderDetailsRequest.setQuantity(cartItem.getValue().getQuantity());
            OrderDetailsResponse orderDetailsResponse = orderDetailsService.createOrderDetails(orderDetailsRequest);
            orderDetailsResponses.add(orderDetailsResponse);
        }
        return orderDetailsResponses;
    }

    @Override
    public void updateCouponStorageAndUsedCount(OrderResponse order) {
        String userName = order.getUser().getUserName();
        Integer couponId= order.getCoupon().getCouponId();
        Coupon coupon = couponRepo.findById(couponId)
                .orElseThrow(() -> new EntityNotFoundException("Not found Coupon"));
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        CouponStorage couponStorage= couponStorageRepo.getCouponStorageByUserNameAndCouponId(userName, couponId);
        couponStorageService.removeCouponInStorage(couponStorage.getCouponStorageId());

    }

    @Override
    public void updateQuantityStock(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Not found Order"));
        List<FoodVariations> updatedVariations = new ArrayList<>();

        order.getOrderDetails().forEach(orderDetails -> {
            FoodVariations foodVariations = foodVariationsDao.findById(orderDetails.getFoodVariations().getFoodVariationId())
                    .orElseThrow(() -> new EntityNotFoundException("Not found Food Variations"));
            if (foodVariations.getQuantityStock() < orderDetails.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for Food Variation ID: " + foodVariations.getFoodVariationId());
            }
            foodVariations.setQuantityStock(foodVariations.getQuantityStock() - orderDetails.getQuantity());
            updatedVariations.add(foodVariations);
        });

        foodVariationsDao.saveAll(updatedVariations);
    }

    //    @Override
//    //  Hàm xử lý Coupon Sau khi tạo hóa đơn
//    public void updateCouponStorageAndUsedCount(String username,String couponId){
//        System.out.println();
//        System.out.println("Coupon !" + couponId);
//        // Nếu couponId không null, tìm coupon theo couponId và tăng số lần sử dụng lên 1
//        if (!couponId.equals("null")){
//            Integer couponIdInt = Integer.valueOf(couponId);
//            CouponResponse couponResponse = couponService.getCouponByCouponId(couponIdInt)
//                    .orElseThrow(() -> new EntityNotFoundException("not found Coupon"));
//            CouponRequest couponRequest = couponMapper.convertResToReq(couponResponse);
//            couponRequest.setUsedCount(couponRequest.getUsedCount() +1);
//            Optional<CouponResponse> couponResponseUpdate = couponService.updateCoupon(couponIdInt, couponRequest);
//            // Lấy ra couponStorage chứa username và coupon trong storageCoupon để xóa coupon đi
//            CouponStorageResponse couponStorageResponse = couponStorageService.getCouponStorageByUsernameAndCouponId(username, couponIdInt);
//            // Nếu người dùng sử dụng coupon trực tiếp , sẽ không xóa couponStorage trong bảng couponStorage
//            if(couponStorageResponse != null){
//                System.out.println();
//                System.out.println("couponStorageResponse" + couponStorageResponse);
//                couponStorageService.removeCouponStorage(couponStorageResponse.getId());
//            }
//        }
//
//    }
    @Override
    public List<OrderDetailsResponse> createOrderDetail(String OrderInfo,Integer orderId){
        // Tạo 1 list Chứa hóa đơn chi tiết chứa các hóa đơn chi tiết sau khi lưu vào database thông qua service( hiện chưa xài tới
        List<OrderDetailsResponse> orderDetailResponses = new ArrayList<>();
        Integer cartId = Integer.valueOf(OrderInfo);
        // Lấy card ra từ Map<cardId,Cart> CartStore . Sau này cần nâng cấp lấy ra từ database
        Cart cart = cartService.getCart(cartId);
        // Trong Cart chứa id và Map items . Map<productVaId, CartItem>items . CartItem chứa productVaId,quantity,price,ProductVaRes
        // Lấy Map items để duyệt vòng forEach qua từng CartItem ~~ 1 orderDetails
        for(Map.Entry<Integer, CartItem> cartItem: cart.getItems().entrySet()){
            OrderDetailsRequest orderDetailRequest  = new OrderDetailsRequest();
            orderDetailRequest.setPrice(cartItem.getValue().getPrice());
            orderDetailRequest.setQuantity(cartItem.getValue().getQuantity());
            orderDetailRequest.setFoodVaId(cartItem.getValue().getFoodVariation().getFoodVariationId());
            orderDetailRequest.setOrderId(orderId);
            OrderDetailsResponse orderDetailResponse= orderDetailsService.createOrderDetails(orderDetailRequest);

            // Sau khi lưu thành công, cập nhật quantity_stock
//            if(orderDetailResponse != null ) {
//                ProductVariationResponse productVariationResponse = productVariationService.getProductVariationById(cartItem.getKey())
//                        .orElseThrow(() -> new EntityNotFoundException("not found productVa"));
//                // Lấy số lượng của từng ProductVa trong cartIem .
//                productVariationResponse.setQuantityStock(productVariationResponse.getQuantityStock() - cartItem.getValue().getTotalQuantity());
//                ProductVariationRequest productVariationRequest =productVariationMapper.convertResToReq(productVariationResponse);
//                // Chuyển sang Request và tiến hành cập nhật vào database
//                Optional<ProductVariationResponse> productVariationResponseUpdated= productVariationService.updatePoducVariation(productVariationRequest, cartItem.getKey());
//            }
            orderDetailResponses.add(orderDetailResponse);
        }
        return orderDetailResponses;
    }


    // Xử Lý nghiệp vụ liên quan đến coupon
    @Override
    public void sendEmail(OrderResponse orderResponse) throws IOException, WriterException {
        MailInfo mail= new MailInfo();
//        UserResponse user = userService.getUserByUsername(username)
//                .orElseThrow(()-> new EntityNotFoundException("not foud user"));
//        mail.setTo(user.getEmail());
        mail.setTo("huong18t4vsl1@gmail.com");
        mail.setSubject("Your Invoices Information ");
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("<html><body>");
        bodyBuilder.append("<p>Kính chào quý khách,</p>");
        bodyBuilder.append("<p>Cảm ơn quý khách đã mua hàng tại HgShop. Dưới đây là thông tin hóa đơn của quý khách:</p>");
//        bodyBuilder.append("<p><strong>Tên khách hàng:</strong> ").append(user.getUsername()).append("<br>");
//        bodyBuilder.append("<strong>Email:</strong> ").append(user.getEmail()).append("<br>");
//        bodyBuilder.append("<strong>Số điện thoại:</strong> 0").append(user.getPhonenumber()).append("<br>");
        bodyBuilder.append("<strong>Tổng tiền:</strong> ").append(orderResponse.getTotalPrice()).append(" VND</p>");
        bodyBuilder.append("<p>Thông tin chi tiết:</p>");
        bodyBuilder.append("<ul>");

        // Vòng lặp để hiển thị từng sản phẩm
        orderResponse.getOrderDetails().forEach(orderDetail -> {
            bodyBuilder.append("<li><strong>Tên Sản Phẩm:</strong> ")
                    .append(orderDetail.getFoodVariations().getFood().getFoodName())
                    .append("<br><strong>Số Lượng:</strong> ").append(orderDetail.getQuantity())
                    .append("<br><strong>Giá Tiền:</strong> ").append(orderDetail.getPrice())
                    .append(" VND</li>");
        });
        bodyBuilder.append("</ul>");
        bodyBuilder.append("<p>Quý khách có thể quét Mã QR để xem sản phẩm chi tiết.</p>");
        bodyBuilder.append("<p>Chúc quý khách có một ngày vui vẻ!</p>");
        bodyBuilder.append("<p>Trân trọng,<br>Công ty Victory Restaurant.</p>");
        bodyBuilder.append("</body></html>");

        mail.setBody(bodyBuilder.toString());
        List<File> files = new ArrayList<>();
        //File qrCodeFile= qrCodeService.createQrCodeWithFileTemp(user.getUsername(), 360, 360);
        File qrCodeFile= qrCodeService.createQrCodeWithFileTemp("huongpham", 360, 360);
        files.add(qrCodeFile);
        mail.setFiles(files);
        try {
            mailService.send(mail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PaymentMethodResponse> getAllPaymentMethod() {
        List<PaymentMethod> paymentMethods = paymentMethodRepo.findAll();
        return paymentMethods.stream()
                .map(paymentMethodMapper :: convertEnToRes)
                .collect(Collectors.toList());
    }
}
