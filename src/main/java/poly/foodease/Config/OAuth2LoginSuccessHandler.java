package poly.foodease.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import poly.foodease.Mapper.UserMapper;
import poly.foodease.Model.Entity.Role;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Request.UserRequest;
import poly.foodease.Model.Response.AuthResponse;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Repository.UserRepo;
import poly.foodease.Service.AuthService;
import poly.foodease.Service.UserService;
import poly.foodease.ServiceImpl.UserDetailServiceImpl;
import poly.foodease.Utils.JwtUtils;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
       // System.out.println("OAuth2User attributes: " + oAuth2User.getAttributes());
        String email = oAuth2User.getAttribute("email");

// Kiểm tra xem email có null hoặc rỗng không
        if (email == null || email.isEmpty()) {
            // Nếu email không có, lấy tên đăng nhập (login) và gán mặc định email
            String login = oAuth2User.getAttribute("login");
            if (login != null) {
                email = login + "@gmail.com";  // Sử dụng login để tạo email mặc định
            } else {
                email = "default@example.com";  // Nếu không có login, gán email mặc định
            }
        }
      //  System.out.println("Email: " + email);
        List<String> roles = new ArrayList<>();
        // Tìm người dùng trong database
        Optional<User> userExist = userRepo.findUserByEmail(email);
//        System.out.println(userExist);
        // Nếu người dùng đã tồn tại, lấy vai trò từ database
        User newUser = null;
        Integer userId ;
        if (userExist.isPresent()) {
            User user = userExist.get();
         //   System.out.println("User Da Ton Tai ");
         //   System.out.println(user);
            // Giả sử User có phương thức getRoles() trả về danh sách các vai trò
            roles = user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getRoleName()) // Thêm "ROLE_" vào trước tên vai trò
                    .collect(Collectors.toList());

           // System.out.println("ROLE In success : " + roles);
            userId= user.getUserId();
        } else {
            System.out.println("USER MOI ");
            // Nếu người dùng chưa tồn tại, tạo người dùng mới và gán vai trò mặc định
            UserRequest userRequest = new UserRequest();
            userRequest.setUserName(email);
            userRequest.setEmail(email);
            userRequest.setPassword("123");
            userRequest.setRoleIds(List.of(2));  // Giả sử 1 là ID của ROLE_USER
            System.out.println("KHONG THAY USER");
            // Tạo người dùng mới và lưu vào cơ sở dữ liệu
            roles.add("ROLE_USER");
            newUser = userRepo.save(userMapper.convertReqToEn(userRequest));
            userId= newUser.getUserId();
           // System.out.println("userId : " + userId);
        }

        // Tạo JWT token
        String token = jwtUtils.generateTokenWithRoles(email, roles);
      //  System.out.println("Generated JWT Token: " + token);

        // Tạo đối tượng AuthResponse chứa thông tin người dùng và token
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUserId(userId);
        authResponse.setUsername(email);
        authResponse.setRoles(roles);
        authResponse.setJwtToken(token);


        // Chuyển đối tượng authResponse thành chuỗi JSON
        String authResponseJson = new ObjectMapper().writeValueAsString(authResponse);

        // Mã hóa chuỗi JSON thành Base64
        String encodedAuthResponse = Base64.getEncoder().encodeToString(authResponseJson.getBytes());

        // Tạo URL và truyền tham số token
        String redirectUrl = "http://localhost:3000/oauth2/redirect?token=" + encodedAuthResponse;

        // Chuyển hướng người dùng đến URL frontend với token JWT
        response.sendRedirect(redirectUrl);

//        // Mã hóa token để an toàn khi truyền qua URL
//        String redirectUrl = "http://localhost:3000/oauth2/redirect?token=" + URLEncoder.encode(String.valueOf(authResponse), StandardCharsets.UTF_8);
//
//        // Chuyển hướng người dùng đến URL frontend với token JWT
//        response.sendRedirect(redirectUrl);
    }

}
