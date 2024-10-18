package poly.foodease.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import poly.foodease.Utils.JwtUtils;

import java.io.IOException;

public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        // Log header để kiểm tra token từ frontend
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        // Kiểm tra token có tồn tại và bắt đầu bằng "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            // Lấy username từ JWT token
            try {
                username = jwtUtils.extractUsername(jwtToken);
            } catch (Exception e) {
                System.out.println("Error extracting username from JWT: " + e.getMessage());
            }
            // Xác thực token nếu username không null và chưa có authentication
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                // Xác minh token hợp lệ
                if (jwtUtils.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // Đặt thông tin xác thực vào SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("Invalid JWT Token");
                }
            }
        } else {
            System.out.println("Authorization header is missing or doesn't start with Bearer.");
        }
        // Tiếp tục chuỗi filter
        chain.doFilter(request, response);
    }
}
