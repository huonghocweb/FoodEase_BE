package poly.foodease.Service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatbotService {

    public Map<String, Object> processRequest(Map<String, Object> request) {
        // Lấy thông tin Intent từ request của Dialogflow
        String intentName = (String) ((Map) request.get("queryResult")).get("intent");

        Map<String, Object> response = new HashMap<>();
        
        switch (intentName) {
            case "Gửi lời chào":
                response = greetUser();
                break;
            case "Tư vấn hoặc hỗ trợ":
                response = getSupportOptions();
                break;
            case "Tư vấn về sản phẩm":
                response = getProductAdvice();
                break;
            case "Hỗ trợ tài khoản":
                response = getAccountSupport();
                break;
            case "Thông tin liên hệ":
                response = getContactInfo();
                break;
            case "Xem chính sách CSKH":
                response = getCustomerSupportPolicy();
                break;
            default:
                response.put("fulfillmentText", "Xin lỗi tôi không hiểu ý bạn! Mọi thông tin thắc mắc vui lòng liên hệ hotline: 0394336606.");
        }
        return response;
    }

    // Hàm trả lời "Gửi lời chào"
    private Map<String, Object> greetUser() {
        Map<String, Object> response = new HashMap<>();
        response.put("fulfillmentText", "Chào bạn! Tôi là trợ lý ảo của FoodEase, có thể giúp bạn tìm kiếm và đặt đồ ăn online. Bạn có cần giúp đỡ gì không?");
        return response;
    }

    // Hàm trả lời "Tư vấn hoặc hỗ trợ"
    private Map<String, Object> getSupportOptions() {
        Map<String, Object> response = new HashMap<>();
        response.put("fulfillmentText", "Bạn cần hỗ trợ về vấn đề gì?\n • Tư vấn về sản phẩm\n • Hỗ trợ tài khoản khách hàng\n • Thông tin liên hệ\n • Xem chính sách CSKH");
        return response;
    }

    // Hàm trả lời "Tư vấn về sản phẩm"
    private Map<String, Object> getProductAdvice() {
        Map<String, Object> response = new HashMap<>();
        response.put("fulfillmentText", "Bạn cần tư vấn về loại sản phẩm nào?\n • Tư vấn về món ăn\n • Tư vấn về đồ uống\n • Xem sản phẩm khuyến mại");
        return response;
    }

    // Hàm trả lời "Hỗ trợ tài khoản"
    private Map<String, Object> getAccountSupport() {
        Map<String, Object> response = new HashMap<>();
        response.put("fulfillmentText", "Bạn muốn hỗ trợ về vấn đề gì?\n • Đăng nhập\n • Đăng ký\n • Quên mật khẩu");
        return response;
    }

    // Hàm trả lời "Thông tin liên hệ"
    private Map<String, Object> getContactInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("fulfillmentText", "Mọi thông tin cần hỗ trợ, vui lòng liên hệ qua:\n Hotline: 0394336606\n Email: ntptlinh716@gmail.com");
        return response;
    }

    // Hàm trả lời "Xem chính sách CSKH"
    private Map<String, Object> getCustomerSupportPolicy() {
        Map<String, Object> response = new HashMap<>();
        response.put("fulfillmentText", "1. Đáp ứng nhanh chóng\n2. Chất lượng sản phẩm\n3. Hỗ trợ khách hàng đầy đủ về sản phẩm, đặt hàng và vận chuyển.");
        return response;
    }
}

