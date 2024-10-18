package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import poly.foodease.Model.Request.AuthRequest;
import poly.foodease.Service.AuthService;
import poly.foodease.Service.UserService;
import poly.foodease.ServiceImpl.AuthServiceImpl;
import poly.foodease.Utils.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthApi {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthServiceImpl authResponse;
    @Autowired
    private AuthService authService;
    @Autowired
     private UserService userService;

    @PostMapping("/api/authenticate")
    public ResponseEntity<Object > authenticate(@RequestBody AuthRequest authRequest) throws Exception {
        //authenticationManager.authenticate nhận vào đối tướng chứa thông tin người dùng (ở đây là UsernamePasswordAuthenticationToken)
        // sau đó dùng AuthenticationProvider để xác thực thông tin với database thông qua UserDetailServiceImpl mà bạn cấu hình
//        System.out.println(authRequest.getUsername() + authRequest.getPassword());
//        System.out.println(userService.getUserByUsername(authRequest.getUsername()));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        }catch (AuthenticationException e){
            throw  new Exception("Incorrect username or password",e);
        }
        // nếu xác thực thành công, lấy thông tin người dùng bằng hàm loadUserByUserName trong UserDetailServiceImpl
        final UserDetails userDetails= userDetailsService.loadUserByUsername(authRequest.getUsername());
        // Lúc này dùng userDetails có các thông tin ng dùng vào utils để tạp token
        final  String jwtToken= jwtUtils.generateToken(userDetails);
        Map<String,Object> result= new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","Get Token");
            result.put("data" ,authService.createAuthResponse(userDetails, jwtToken));
        }catch(Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }
}