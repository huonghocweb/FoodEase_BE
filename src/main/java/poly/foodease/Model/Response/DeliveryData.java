package poly.foodease.Model.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class DeliveryData {
    private double distance;
    private double weather;
    private double orderTime;
    private double orderVolume;
}

