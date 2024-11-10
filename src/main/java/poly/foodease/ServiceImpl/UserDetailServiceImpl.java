package poly.foodease.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.UserMapper;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserResponse userResponse = userService.getUserByUsername(username).get();
        if(userResponse == null){
            throw new UsernameNotFoundException("not found user by username");
        }
        List<GrantedAuthority> authorities= userResponse.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+ role.getRoleName()))
                .collect(Collectors.toList());
        // tạo ra đối tượng User của userdetails trong spring , thêm các dữ liệu người dùng từ database vào.
        return new org.springframework.security.core.userdetails.User(userResponse.getUserName(), userResponse.getPassword(),authorities);
    }
}