package poly.foodease.Service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
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
}