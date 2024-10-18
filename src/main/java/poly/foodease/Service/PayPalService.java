package poly.foodease.Service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.foodease.Config.PayPalConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {

    @Autowired
    private PayPalConfig paypalConfig;

    // Định nghĩa các thông số có thể tủy chỉnh như tiền tệ , phương thức
    private static final String CURRENCY = "USD";
    private static final String  METHOD = "paypal";
    // Có 3 loại Intent
    // Capture (SALE) : thanh toán ngay lập tức
    // AUTHORIZE : xác nhận tiền có sẵn và có thể thu sau đó
    // Order : Tạo đơn hàng để thanh toán sau hoặc 1 lần .
    private static final String  INTENT = "SALE";


    // Hàm giúp tạo giao dịch PayPal, người dùng đăng nhập và thanh toán
    public Payment createPayment(
            Double total,
            Integer orderInfo,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {

        Amount amount = new Amount();
        amount.setCurrency(CURRENCY);
        // Chuyển tiền về mệnh giá USD
        total = total/23000;
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(String.valueOf(orderInfo));
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(METHOD.toString());

        // Payment là đối tượng đại diện cho toàn bộ thanh toán
        Payment payment = new Payment();
        payment.setIntent(INTENT.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        // Tạo một APIContext mới cho mỗi yêu cầu
        APIContext apiContext = createAPIContext();

        // Thực hiện yêu cầu thanh toán
        return payment.create(apiContext);
    }

    // Hàm xử lý sao khi hoàn tất thanh toán
    public Payment executePayment(HttpServletRequest request) throws PayPalRESTException {
        // Lấy mã giao dịch vaf mã ng giao dịch từ URL trả về phía fontend
        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");

        // Tạo đối tượng Payment đại diện cho mỗi giao dịch của PayPal và thiết lập paymentID
        Payment payment = new Payment();
        payment.setId(paymentId);

        // PaymentExecution đại diện cho thông tin người dùng thanh toán và thiết lập payerID
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);

        // ApiContext là lớp của Paypal SDK dùng để chứa thông tin cấu hình và xác thực các yêu cầu API đến Paypal.
        // cấu hình trong paypalConfig cùng clientId, clientSecret,môi trường
        APIContext apiContext = createAPIContext();

        // Phương thức excute sẽ yêu cầu Paypal kiểm tra paymentId đã được người dùng xác nhận thanh toán chưa và trả về kết quả.
        return payment.execute(apiContext, paymentExecute);
    }

    // Tạo APIContext mới với AccessToken và cấu hình
    private APIContext createAPIContext() throws PayPalRESTException {
        APIContext apiContext = new APIContext(paypalConfig.oAuthTokenCredential().getAccessToken());
        apiContext.setConfigurationMap(paypalConfig.paypalSdkConfig());
        return apiContext;
    }

//    // Hàm xử lý sao khi hoàn tất thanh toán
//    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
//        Payment payment = new Payment();
//        payment.setId(paymentId);
//        PaymentExecution paymentExecute = new PaymentExecution();
//        paymentExecute.setPayerId(payerId);
//        return payment.execute(apiContext, paymentExecute);
//    }

}
