package poly.foodease.Controller.Api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Model.Request.UserRequest;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Service.UserService;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
public class UserApi {
    @Autowired
    UserService userService;

    @GetMapping("/getByUserName/{userName}")
    public ResponseEntity<Object> getUserByUserName(
            @PathVariable("userName") String userName
    ) throws JsonProcessingException {
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message","get User By UserName");
            result.put("data", userService.getUserByUsername(userName));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(defaultValue = "userName") String sortBy) {

        Page<UserResponse> usersPage = userService.getAllUserByPage(pageNumber, pageSize, sortOrder, sortBy);

        if (usersPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Trả về 204 nếu không có người dùng
        }
        return ResponseEntity.ok(usersPage); // Trả về 200 và danh sách người dùng
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        try {
            UserResponse createdUser = userService.createUser(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest, @PathVariable Integer id) {
        return userService.updateUser(userRequest, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
