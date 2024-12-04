package poly.foodease.Controller.Api;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.foodease.Mapper.UserMapper;
import poly.foodease.Model.Entity.PasswordResetToken;
import poly.foodease.Model.Entity.RegistrationToken;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Request.ResetPasswordRequest;
import poly.foodease.Model.Request.UserRequest;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Repository.PasswordResetTokenRepo;
import poly.foodease.Repository.RoleRepo;
import poly.foodease.Repository.UserRepo;
import poly.foodease.Service.CloudinaryService;
import poly.foodease.Service.EmailService;
import poly.foodease.Service.RegistrationService;
import poly.foodease.Service.UserService;
import poly.foodease.Utils.UserExportExcel;

import poly.foodease.Utils.UserImportExcel;

import poly.foodease.Model.Entity.Role;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")

public class  UserApi {
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
    //  Hoa
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserMapper userMapper;  // Inject userMapper
    @Autowired
    private EmailService emailService;


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
            System.out.println("Role IDs JSON: " + roleIdsJson);

            // Tìm người dùng theo ID
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

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
            List<Integer> roleIds;
            try {
                roleIds = objectMapper.readValue(roleIdsJson, List.class);
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role IDs format");
            }

            // Cập nhật vai trò của người dùng
            List<Role> roles = roleIds.stream()
                    .map(roleId -> roleRepo.findById(roleId)
                            .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + roleId)))
                    .collect(Collectors.toList());
            user.setRoles(roles);

            // Cập nhật ảnh đại diện nếu có
            if (image != null && !image.isEmpty()) {
                System.out.println("Processing new image upload: " + image.getOriginalFilename());
                String imageUrl = cloudinaryService.uploadFile(new MultipartFile[]{image}, "avatars").get(0);
                user.setImageUrl(imageUrl);
            }

            // Lưu người dùng sau khi cập nhật
            User updatedUser = userRepo.save(user);

            // Trả về thông tin người dùng đã cập nhật
            UserResponse userResponse = userMapper.convertEnToRes(updatedUser);
            return ResponseEntity.ok(userResponse);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing image upload");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    //	Hòa
    @PostMapping("/request-reset-password")
    public ResponseEntity<Object> requestPasswordReset(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        String email = request.get("email");
        try {
            String message = userService.requestPasswordReset(email);
            result.put("success", true);
            result.put("message", message);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordRequest request) {
        Map<String, Object> result = new HashMap<>();
        String token = request.getToken();
        String newPassword = request.getNewPassword();

        // Kiểm tra xem token và mật khẩu mới có tồn tại không
        if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            result.put("success", false);
            result.put("message", "Token and new password are required.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        try {
            PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

            if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired.");
            }

            if (resetToken.getUser() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user associated with this token.");
            }

            User user = resetToken.getUser();
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepo.save(user);

            passwordResetTokenRepo.delete(resetToken);

            result.put("success", true);
            result.put("message", "Password reset successful!");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Failed to reset password: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
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
    // Hoa
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

            // Lấy danh sách vai trò của người dùng
            List<Role> roles = user.getRoles();

            result.put("success", true);
            result.put("message", "User details fetched successfully");
            result.put("data", Map.of(
                    "userId", user.getUserId(),
                    "userName", user.getUserName(),
                    "roles", roles.stream().map(role -> Map.of(
                            "roleId", role.getRoleId(),
                            "roleName", role.getRoleName()
                    )).collect(Collectors.toList())
            ));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
    //Hoa
    @PostMapping("/request-registration-code")
    public ResponseEntity<?> requestRegistrationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, Object> response = new HashMap<>();

        try {
            registrationService.deleteToken(email); // Xóa mã xác thực cũ nếu có
            RegistrationToken registrationToken = registrationService.createRegistrationToken(email);
            emailService.sendEmail(email, "Your Registration Code", "Your code is: " + registrationToken.getToken());

            response.put("success", true);
            response.put("message", "Verification code sent to email!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Failed to send verification code.");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/confirm-registration-code")
    public ResponseEntity<?> confirmRegistrationCode(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String email = request.get("email");
        Map<String, Object> response = new HashMap<>();

        Optional<RegistrationToken> registrationToken = registrationService.validateToken(token);

        if (registrationToken.isPresent() && registrationToken.get().getEmail().equals(email)) {
            response.put("success", true);
            response.put("message", "Token verified successfully! Proceed to register.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Invalid or expired token.");
            return ResponseEntity.status(400).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestParam("userName") String userName,
            @RequestParam("fullName") String fullName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("password") String password,
            @RequestParam("address") String address,
            @RequestParam("birthday") String birthday,
            @RequestParam("gender") Boolean gender,
            @RequestParam("roleIds") String roleIdsJson, // JSON chuỗi role IDs
            @RequestParam("email") String email,
            @RequestParam("token") String token,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Map<String, Object> response = new HashMap<>();
        try {
            // Xác thực mã xác thực
            Optional<RegistrationToken> registrationToken = registrationService.validateToken(token);

            if (registrationToken.isEmpty() || !registrationToken.get().getEmail().equals(email)) {
                response.put("success", false);
                response.put("message", "Invalid or expired token.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Chuyển đổi roleIds từ JSON chuỗi thành danh sách Integer
            ObjectMapper objectMapper = new ObjectMapper();
            List<Integer> roleIds = objectMapper.readValue(roleIdsJson, List.class);

            // Kiểm tra nếu danh sách roleIds trống, mặc định là vai trò "User"
            if (roleIds == null || roleIds.isEmpty()) {
                roleIds = Collections.singletonList(2); // Ví dụ: vai trò "User" có ID là 2
            }

            // Chuẩn bị đối tượng UserRequest
            UserRequest userRequest = new UserRequest();
            userRequest.setUserName(userName);
            userRequest.setFullName(fullName);
            userRequest.setPhoneNumber(phoneNumber);
            userRequest.setPassword(password);
            userRequest.setAddress(address);
            userRequest.setBirthday(LocalDate.parse(birthday));
            userRequest.setGender(gender);
            userRequest.setRoleIds(roleIds);
            userRequest.setEmail(email);

            // Tạo người dùng mới
            UserResponse newUser = userService.createUser(userRequest);

            // Lưu ảnh nếu có
            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(new MultipartFile[]{image}, "avatars").get(0);
                newUser.setImageUrl(imageUrl); // Cập nhật URL ảnh vào response người dùng mới
            }

            // Xóa mã xác thực sau khi đăng ký thành công
            registrationService.deleteToken(email);

            // Chuẩn bị response
            response.put("success", true);
            response.put("message", "User registered successfully!");
            response.put("data", newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Failed to process role IDs.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Failed to register user.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


//	Hòa



    // Ngoc
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
                row.createCell(3).setCellValue(user.getGender());
                row.createCell(4).setCellValue(user.getAddress());
                row.createCell(5).setCellValue(user.getPhoneNumber());
                row.createCell(6).setCellValue(user.getImageUrl());
                row.createCell(7).setCellValue(user.getBirthday().toString());
                row.createCell(8).setCellValue(user.getEmail());
                row.createCell(9).setCellValue(user.getStatus());
            }

            // Ghi dữ liệu vào byte array
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                byte[] bytes = out.toByteArray();
                return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=users.xlsx").body(bytes);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ngoc


    @PostMapping("/importUser")
    public ResponseEntity<String> importUserBuy(@RequestParam("file") MultipartFile file) {
        System.out.println("Import ");
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tệp không hợp lệ.");
        }
// Nhawsc
        String directoryPath = "C:\\Users\\ASUS\\Downloads\\files\\";
        String filePath = directoryPath + file.getOriginalFilename(); // Đường dẫn đầy đủ đến tệp

        // Tạo thư mục nếu chưa tồn tại
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Tạo thư mục và tất cả thư mục con nếu cần
        }

        try {
            // Lưu tệp tải lên tạm thời
            File tempFile = new File(filePath);
            file.transferTo(tempFile);

            // Tạo đối tượng ExcelImporter và gọi importExcelData
            UserImportExcel excelImporter = new UserImportExcel(userService);
            excelImporter.importExcelData(tempFile.getAbsolutePath());

            return ResponseEntity.ok("Dữ liệu đã được nhập thành công.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nhập dữ liệu thất bại.");
        }

    }
    @GetMapping("/exportUser")
    public void exportUserBuy(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<User> list = userService.getAllUsers();
        UserExportExcel excelExporter = new UserExportExcel(list);

        excelExporter.export(response);

    }
}
