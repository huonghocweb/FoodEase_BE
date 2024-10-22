package poly.foodease.Controller.Api;

import poly.foodease.Service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatBotApi {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping
    public Map<String, Object> handleChatbotRequest(@RequestBody Map<String, Object> request) {
        // Gọi service để xử lý request và trả về phản hồi cho Dialogflow
        return chatbotService.processRequest(request);
    }
}
