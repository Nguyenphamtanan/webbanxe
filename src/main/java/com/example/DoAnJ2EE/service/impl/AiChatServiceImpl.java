package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.dto.AiChat.AiChatResponse;
import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.repository.MotorbikeRepository;
import com.example.DoAnJ2EE.service.AiChatService;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final MotorbikeRepository motorbikeRepository;

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    @Override
    public String chat(String message, Long motorbikeId, String pageContext) {
        if (message == null || message.trim().isEmpty()) {
            throw new RuntimeException("Thiếu nội dung câu hỏi");
        }

        List<Map<String, Object>> latestMotorbikes = motorbikeRepository.findAll()
                .stream()
                .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .limit(8)
                .map(this::mapMotorbikeSummary)
                .toList();

        Map<String, Object> currentMotorbike = null;
        if (motorbikeId != null) {
            currentMotorbike = motorbikeRepository.findById(motorbikeId)
                    .map(this::mapMotorbikeDetail)
                    .orElse(null);
        }

        String prompt = buildPrompt(message, pageContext, latestMotorbikes, currentMotorbike);

        try (Client client = Client.builder().apiKey(apiKey).build()) {
            GenerateContentResponse response =
                    client.models.generateContent(model, prompt, null);

            String answer = response.text();
            return (answer == null || answer.isBlank())
                    ? "Xin lỗi, hiện chưa có câu trả lời phù hợp."
                    : answer;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi gọi Gemini: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> mapMotorbikeSummary(Motorbike motorbike) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", motorbike.getId());
        item.put("name", motorbike.getName());
        item.put("slug", motorbike.getSlug());
        item.put("sku", motorbike.getSku());
        item.put("price", motorbike.getPrice());
        item.put("engine", motorbike.getEngine());
        item.put("weightKg", motorbike.getWeightKg());
        item.put("brand", motorbike.getBrand() != null ? motorbike.getBrand().getName() : null);
        item.put("category", motorbike.getCategory() != null ? motorbike.getCategory().getName() : null);
        return item;
    }

    private Map<String, Object> mapMotorbikeDetail(Motorbike motorbike) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", motorbike.getId());
        item.put("name", motorbike.getName());
        item.put("slug", motorbike.getSlug());
        item.put("sku", motorbike.getSku());
        item.put("price", motorbike.getPrice());
        item.put("description", motorbike.getDescription());
        item.put("engine", motorbike.getEngine());
        item.put("weightKg", motorbike.getWeightKg());
        item.put("primaryImageUrl", motorbike.getPrimaryImageUrl());
        item.put("brand", motorbike.getBrand() != null ? motorbike.getBrand().getName() : null);
        item.put("category", motorbike.getCategory() != null ? motorbike.getCategory().getName() : null);
        item.put("isActive", motorbike.getIsActive());
        return item;
    }

    private String buildPrompt(String message,
                               String pageContext,
                               List<Map<String, Object>> latestMotorbikes,
                               Map<String, Object> currentMotorbike) {

        return """
Bạn là trợ lý AI cho website bán xe máy.
Chỉ trả lời bằng tiếng Việt.
Giọng văn lịch sự, ngắn gọn, rõ ràng.
Chỉ dùng dữ liệu được cung cấp bên dưới, không tự bịa thêm.
Nếu hệ thống chưa có dữ liệu thì phải nói rõ: "Hiện hệ thống chưa có thông tin này".
Không được tự bịa giá, hãng, loại xe, tình trạng xe hoặc chương trình khuyến mãi.
Nếu người dùng hỏi ngoài phạm vi website, hãy trả lời ngắn gọn và điều hướng họ xem sản phẩm trên website.

Ngữ cảnh trang:
""" + String.valueOf(pageContext) + """

Danh sách xe mới / đang hiển thị:
""" + String.valueOf(latestMotorbikes) + """

Xe người dùng đang xem:
""" + String.valueOf(currentMotorbike) + """

Quy tắc trả lời:
- Nếu đang có dữ liệu xe người dùng đang xem thì ưu tiên tư vấn theo xe đó.
- Nếu không có xe cụ thể, có thể gợi ý từ danh sách xe bên trên.
- Nếu người dùng hỏi xe nào phù hợp đi học, đi làm, tiết kiệm chi phí thì dựa vào loại xe, giá, động cơ, mô tả.
- Không nói những gì hệ thống không có.
- Không trả lời lan man.
- Câu trả lời tối đa khoảng 5 câu.

Câu hỏi người dùng:
""" + message;
    }
}