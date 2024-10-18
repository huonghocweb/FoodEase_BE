package poly.foodease.Service;


import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import poly.foodease.Config.MomoConfig;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MomoService{

    @Autowired
    private MomoConfig momoConfig;
    // đường dẫn đến cổng thanh toán sandbox của momo
    @Value("${momo.endpoint}")
    private String endpoint;

    @Value("${momo.ipnUrl}")
    private String ipnUrl;
//
//    @Value("${momo.endpointVerify}")
//    private String endpointVerify;

    public static final String URL_RETURN = "/thanks/momo" ;


    public static final String PAYMENT_METHOD = "payWithATM" ;
    // public static final String PAYMENT_METHOD = "captureWallet" ;

    public String createPaymentRequest(Integer orderInfo, long amount, String baseUrlReturn,String username) {
        // Tạo chuỗi ngẫu nhiễn làm orderId
        String orderId ="MOMO" + UUID.randomUUID().toString().substring(0, 5);
        try {
            // Tạo body yêu cầu
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("partnerCode", momoConfig.getPartnetCode());
            requestBody.put("accessKey", momoConfig.getAccessKey());
            requestBody.put("requestId", orderId);
            requestBody.put("amount", String.valueOf(amount));
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", String.valueOf(orderInfo));
            requestBody.put("redirectUrl", baseUrlReturn + URL_RETURN);
            requestBody.put("ipnUrl", ipnUrl);  // Đường dẫn public mà momo sẽ trả về
            requestBody.put("requestType", PAYMENT_METHOD);
            requestBody.put("lang", "vi");
            requestBody.put("extraData", username);  // Đảm bảo trường này không null

            // Tính toán chữ ký (signature)
            String rawData = "accessKey=" + momoConfig.getAccessKey() +
                    "&amount=" + amount +
                    "&extraData=" + username +
                    "&ipnUrl=" + ipnUrl +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + momoConfig.getPartnetCode() +
                    "&redirectUrl=" + baseUrlReturn + URL_RETURN +
                    "&requestId=" + orderId +
                    "&requestType=" + PAYMENT_METHOD;


            // Tạo chữ ký đưa vào request để Momo tạo ra ra chữ ký khác và kiểm tra để đảm baor dữ liệu không bị thay đổi
            String signature = HmacSHA256(rawData, momoConfig.getSecertKey());
            requestBody.put("signature", signature);

            // Gọi API MoMo bằng RestTemplate
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            // Thiết lập header chứa kiểu dữ liệu dạng JSON
            headers.setContentType(MediaType.APPLICATION_JSON);

            //HttpEntity là đối tượng đại diện cho yêu cầu HTTP . Truyền requestBody và header đã thiết lập vào
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
            // Call Post Api đến endpoint của Momo và lưu phản hồi vào response.
            ResponseEntity<String> response = restTemplate.postForEntity(endpoint, entity, String.class);

            // Xử lý phản hồi
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject responseObject = new JSONObject(response.getBody());
                if (responseObject.has("payUrl")) {
                    return responseObject.getString("payUrl");
                } else {
                    System.err.println("Response does not contain payUrl: " + response.getBody());
                }
            } else {
                System.err.println("Failed to create payment request: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Integer verifyPayment(Map<String, String> ipnData) throws Exception {

        // Lấy dữ liệu từ ipnData
        String partnerCode = ipnData.get("partnerCode");
        String orderId = ipnData.get("orderId");
        String requestId = ipnData.get("requestId");
        String amount = ipnData.get("amount");
        String orderInfo = ipnData.get("orderInfo");
        String orderType = ipnData.get("orderType");
        String transId = ipnData.get("transId");
        String resultCode = ipnData.get("resultCode");
        String message = ipnData.get("message");
        String payType = ipnData.get("payType");
        String responseTime = ipnData.get("responseTime");
        // Là chữ ký của momo tạo ra , sẽ dùng để so sánh với chữ ký tạo ra từ dữ liệu được momo trả về cùng
        String signature = ipnData.get("signature");

        // Tạo chuỗi rawData để xác thực chữ ký
        String rawData = "accessKey=" + momoConfig.getAccessKey() +
                "&amount=" + amount +
                "&extraData=" + ipnData.get("extraData") +
                "&message=" + message +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&orderType=" + orderType +
                "&partnerCode=" + partnerCode +
                "&payType=" + payType +
                "&requestId=" + requestId +
                "&responseTime=" + responseTime +
                "&resultCode=" + resultCode +
                "&transId=" + transId;

        // Tạo ra chữ ký từ các data momo trả về ipn
        String expectedSignature = HmacSHA256(rawData, momoConfig.getSecertKey());
        // So sánh chữ ký momo tạo ra và chữ ký do data từ momo trả về tạo ra
        System.out.println("signature :" + signature);
        System.out.println("expectedSignature :" +expectedSignature);
        if (expectedSignature.equals(signature)) {

            // Thanh toán thành công
            System.out.println("Payment verified successfully");
            return 0;
        } else {
            // Chữ ký không hợp lệ
            System.out.println("Invalid signature");
            return 1;
        }
    }



    // Hàm để tạo signature HmacSHA256
    private String HmacSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes());
        return Hex.encodeHexString(hash);
    }


}

