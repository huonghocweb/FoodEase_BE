package poly.foodease.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MomoService {

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.endpoint}")
    private String endpoint;

    @Value("${momo.ipnUrl}")
    private String ipnUrl;

    private static final String URL_RETURN = "/thanks/momo";
   // private static final String PAYMENT_METHOD = "captureWallet";
    private static final String PAYMENT_METHOD = "payWithATM";

    public String createPaymentRequest(String orderInfo, long amount, String baseUrlReturn, String username) {
        String orderId = "MOMO" + UUID.randomUUID().toString().substring(0, 5);
        try {
            // Tạo body yêu cầu
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("partnerCode", partnerCode);
            requestBody.put("accessKey", accessKey);
            requestBody.put("requestId", orderId);
            requestBody.put("amount", String.valueOf(amount));
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", baseUrlReturn + URL_RETURN);
            requestBody.put("ipnUrl", ipnUrl);
            requestBody.put("requestType", PAYMENT_METHOD);
            requestBody.put("lang", "vi");
            requestBody.put("extraData", username != null ? username : "");

            // Tính chữ ký (signature)
            String rawData = String.format(
                    "accessKey=%s&amount=%s&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                    accessKey, amount, username != null ? username : "", ipnUrl, orderId, orderInfo, partnerCode,
                    baseUrlReturn + URL_RETURN, orderId, PAYMENT_METHOD
            );

            String signature = HmacSHA256(rawData, secretKey);
            requestBody.put("signature", signature);

            // Gọi API MoMo
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(endpoint, entity, String.class);

            // Xử lý phản hồi
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject responseObject = new JSONObject(response.getBody());
                if (responseObject.has("payUrl")) {
                    return responseObject.getString("payUrl");
                } else {
                    System.err.println("Response does not contain payUrl: " + responseObject.toString());
                }
            } else {
                System.err.println("Failed to create payment request. Status Code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error during createPaymentRequest: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Integer verifyPayment(Map<String, String> ipnData) {
        try {
            // Lấy dữ liệu từ ipnData
            String rawData = String.format(
                    "accessKey=%s&amount=%s&extraData=%s&message=%s&orderId=%s&orderInfo=%s&orderType=%s&partnerCode=%s&payType=%s&requestId=%s&responseTime=%s&resultCode=%s&transId=%s",
                    accessKey, ipnData.get("amount"), ipnData.get("extraData"), ipnData.get("message"), ipnData.get("orderId"),
                    ipnData.get("orderInfo"), ipnData.get("orderType"), ipnData.get("partnerCode"), ipnData.get("payType"),
                    ipnData.get("requestId"), ipnData.get("responseTime"), ipnData.get("resultCode"), ipnData.get("transId")
            );

            // So sánh chữ ký
            String expectedSignature = HmacSHA256(rawData, secretKey);
            if (expectedSignature.equals(ipnData.get("signature"))) {
                System.out.println("Payment verified successfully.");
                return 0; // Thành công
            } else {
                System.err.println("Invalid signature.");
                return 1; // Chữ ký không hợp lệ
            }
        } catch (Exception e) {
            System.err.println("Error during verifyPayment: " + e.getMessage());
            e.printStackTrace();
            return 2; // Lỗi khác
        }
    }

    private String HmacSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKeySpec);
        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes()));
    }
}
