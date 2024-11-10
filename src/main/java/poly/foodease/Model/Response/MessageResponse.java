package poly.foodease.Model.Response;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    private String message;
    private int code;
    private Object data;
    private LocalDate date;
}
