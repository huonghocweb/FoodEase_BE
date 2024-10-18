package poly.foodease.Config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class MomoConfig {

    @Value("${momo.partnerCode}")
    private String partnetCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secertKey;

    @Value("${momo.endpoint}")
    private String endpoint;

    @Value("${momo.ipnUrl}")
    private String ipnUrl;
}
