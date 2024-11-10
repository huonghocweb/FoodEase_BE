package poly.foodease.Service;

import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Request.UserRequest;
import poly.foodease.Model.Response.UserResponse;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    Page<UserResponse> getAllUserByPage(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);
    List<UserResponse> getAllUser();
    Optional<UserResponse> getUserById(Integer id);
    UserResponse createUser(UserRequest userRequest);
    Optional<UserResponse> updateUser(UserRequest userRequest, Integer id);
    Optional<Void> deleteUserById(Integer id);
    Optional<UserResponse> getUserByUsername(String username);
    List<User> getAllUsers();
    void saveAll(List<User> users);
    User SaveUser(User user);
    String requestPasswordReset(String email) throws MessagingException;
    String resetPassword(String token, String newPassword);
    String requestRegisterCode(String email) throws MessagingException;
}