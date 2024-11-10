package poly.foodease.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Cấu hình CORS
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/authenticate/**").permitAll() // Cho phép truy cập không cần xác thực
//                        .requestMatchers("/api/**").authenticated()
//                        .anyRequest().permitAll() // Các yêu cầu khác đều không yêu cầu xác thực
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .successHandler(oAuth2LoginSuccessHandler)) // Xử lý khi login thành công
//                        //.failureHandler(oAuth2LoginFailureHandler)) // Xử lý khi login thất bại
//                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class); // Thêm bộ lọc JWT
//        return httpSecurity.build();
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Cấu hình CORS
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/user/request-registration-code",
                        "/api/user/register","/api/user/confirm-registration-code",
                        "/api/user/confirm-reset-password","/api/user/request-reset-password",
                        "/api/user/reset-password", "/api/authenticate/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)) // Xử lý khi login thành công
                       //.failureHandler(oAuth2LoginFailureHandler)) // Xử lý khi login thất bại
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class); // Thêm bộ lọc JWT
        return httpSecurity.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://*.example.com")); // Cấu hình nguồn cụ thể
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Các phương thức được phép
        configuration.setAllowCredentials(true); // Cho phép gửi thông tin nhạy cảm (cookies, header authentication)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // Cấu hình các header cho phép
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Mã hóa mật khẩu
    }
    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(); // Bộ lọc JWT tùy chỉnh
    }
}
