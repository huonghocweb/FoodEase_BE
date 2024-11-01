package poly.foodease.Controller.Api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.foodease.Mapper.UserMapper;
import poly.foodease.Model.Entity.PasswordResetToken;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Request.ResetPasswordRequest;
import poly.foodease.Model.Request.UserRequest;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Repository.PasswordResetTokenRepo;
import poly.foodease.Repository.RoleRepo;
import poly.foodease.Repository.UserRepo;
import poly.foodease.Service.CloudinaryService;
import poly.foodease.Service.UserService;
import poly.foodease.Model.Entity.Role;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
public class UserApi {
    @Autowired
    UserService userService;
    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private RoleRepo roleRepo;  // Inject roleRepo

    @Autowired
    private UserMapper userMapper;  // Inject userMapper
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
    public ResponseEntity<Object> updateUser(
            @PathVariable Integer id,
            @RequestParam("userName") String userName,
            @RequestParam("fullName") String fullName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam("birthday") String birthday,
            @RequestParam("gender") Boolean gender,
            @RequestParam("roleIds") String roleIdsJson,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            // Log dữ liệu nhận được từ client
            System.out.println("Updating user with ID: " + id);
            System.out.println("UserName: " + userName);
            System.out.println("FullName: " + fullName);
            System.out.println("PhoneNumber: " + phoneNumber);
            System.out.println("Email: " + email);
            System.out.println("Address: " + address);
            System.out.println("Birthday: " + birthday);
            System.out.println("Gender: " + gender);
            System.out.println("Role IDs JSON: " + roleIdsJson);

            // Tìm người dùng theo ID
            Optional<User> optionalUser = userRepo.findById(id);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            User user = optionalUser.get();

            // Cập nhật các thuộc tính của người dùng
            user.setUserName(userName);
            user.setFullName(fullName);
            user.setPhoneNumber(phoneNumber);
            user.setEmail(email);
            user.setAddress(address);
            user.setBirthday(LocalDate.parse(birthday));
            user.setGender(gender);

            // Chuyển đổi roleIds từ JSON sang danh sách ID
            ObjectMapper objectMapper = new ObjectMapper();
            List<Integer> roleIds = objectMapper.readValue(roleIdsJson, List.class);

            // Cập nhật vai trò của người dùng
            List<Role> roles = roleIds.stream()
                    .map(roleId -> roleRepo.findById(roleId)
                            .orElseThrow(() -> new EntityNotFoundException("Role not found for ID: " + roleId)))
                    .collect(Collectors.toList());
            user.setRoles(roles);

            // Cập nhật ảnh đại diện nếu có
            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(new MultipartFile[]{image}, "avatars").get(0);
                user.setImageUrl(imageUrl);
            }

            // Lưu người dùng sau khi cập nhật
            userRepo.save(user);

            // Trả về thông tin người dùng đã cập nhật
            UserResponse userResponse = userMapper.convertEnToRes(user);
            return ResponseEntity.ok(userResponse);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role IDs format");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/import")
    public ResponseEntity<String> importUsers(@RequestParam("file") MultipartFile file) {
        List<User> userList = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            var sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Bỏ qua dòng tiêu đề
                Row row = sheet.getRow(i);

                User user = User.builder()
                        .userName(row.getCell(0).getStringCellValue())
                        .fullName(row.getCell(1).getStringCellValue())
                        .email(row.getCell(2).getStringCellValue())
                        .phoneNumber(row.getCell(3).getStringCellValue())
                        .address(row.getCell(4).getStringCellValue())
                        .birthday(row.getCell(5).getLocalDateTimeCellValue().toLocalDate())
                        .gender(row.getCell(6).getStringCellValue().equalsIgnoreCase("Male"))
                        .build();
                userList.add(user);
            }
            userService.saveAll(userList); // Lưu tất cả người dùng vào DB
            return ResponseEntity.ok("Import successful!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to import users: " + e.getMessage());
        }
    }


    @GetMapping("/export")
    public ResponseEntity<byte[]> exportUsers() {
        List<User> userList = userService.getAllUsers(); // Lấy tất cả người dùng từ DB
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Users");

            // Tạo tiêu đề cột
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("User Name");
            headerRow.createCell(1).setCellValue("Full Name");
            headerRow.createCell(2).setCellValue("Password");
            headerRow.createCell(3).setCellValue("Gender");
            headerRow.createCell(4).setCellValue("Address");
            headerRow.createCell(5).setCellValue("Phone Number");
            headerRow.createCell(6).setCellValue("Image URL");
            headerRow.createCell(7).setCellValue("Birthday");
            headerRow.createCell(8).setCellValue("Email");
            headerRow.createCell(9).setCellValue("Status");

            // Điền dữ liệu vào file
            int rowNum = 1;
            for (User user : userList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getUserName());
                row.createCell(1).setCellValue(user.getFullName());
                row.createCell(2).setCellValue(user.getPassword());
                row.createCell(3).setCellValue(user.getGender() ? "Male" : "Female");
                row.createCell(4).setCellValue(user.getAddress());
                row.createCell(5).setCellValue(user.getPhoneNumber());
                row.createCell(6).setCellValue(user.getImageUrl());
                row.createCell(7).setCellValue(user.getBirthday() != null ? user.getBirthday().toString() : "");
                row.createCell(8).setCellValue(user.getEmail());
                row.createCell(9).setCellValue(user.getStatus() ? "Active" : "Inactive");
            }

            // Ghi dữ liệu vào byte array
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                byte[] bytes = out.toByteArray();
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=users.xlsx")
                        .body(bytes);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/request-reset-password")
    public ResponseEntity<Object> requestPasswordReset(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        String email = request.get("email"); // Lấy email từ request body
        try {
            String message = userService.requestPasswordReset(email);
            result.put("success", true);
            result.put("message", message);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

//    @PostMapping("/reset-password")
//    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordRequest request) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            String message = userService.resetPassword(request.getToken(), request.getNewPassword());
//            result.put("success", true);
//            result.put("message", message);
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", e.getMessage());
//            return ResponseEntity.status(500).body(result);
//        }
//    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordRequest request) {
        Map<String, Object> result = new HashMap<>();
        String token = request.getToken(); // Mã số xác nhận
        String newPassword = request.getNewPassword();
        try {
            PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid or expired token!"));

            // Kiểm tra thời gian hết hạn của mã số
            if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired.");
            }
            // Cập nhật mật khẩu mới cho người dùng sau khi mã hóa
            User user = resetToken.getUser();
            String encodedPassword = passwordEncoder.encode(newPassword); // Mã hóa mật khẩu mới
            user.setPassword(encodedPassword);
            userRepo.save(user);
            // Xóa token sau khi sử dụng
            passwordResetTokenRepo.delete(resetToken);
            result.put("success", true);
            result.put("message", "Password reset successful!");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
    @PostMapping("/confirm-reset-password")
    public ResponseEntity<Object> confirmResetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String action = request.get("action");
        Map<String, Object> result = new HashMap<>();
        try {
            // Thay đổi kiểu dữ liệu của resetToken thành PasswordResetToken
            PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid token!"));
            if (action.equals("yes")) {
                result.put("success", true);
                result.put("message", "Confirmation success! Redirecting to reset password page.");
            } else {
                // Xóa token nếu người dùng chọn "Không"
                passwordResetTokenRepo.delete(resetToken);
                result.put("success", false);
                result.put("message", "Reset password request cancelled.");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
    @PostMapping("/upload-avatar")
    public ResponseEntity<Map<String, Object>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            // Upload file lên Cloudinary và lấy URL hình ảnh
            List<String> imageUrls = cloudinaryService.uploadFile(new MultipartFile[]{file}, "avatars");
            String imageUrl = imageUrls.get(0); // Giả định rằng luôn có ít nhất 1 URL
            result.put("success", true);
            result.put("imageUrl", imageUrl);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "Failed to upload avatar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }


}
