package poly.foodease.Controller.Api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Request.UserRequest;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Service.UserService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                        .password(row.getCell(2).getStringCellValue())
                        .gender(row.getCell(3).getBooleanCellValue())
                        .address(row.getCell(4).getStringCellValue())
                        .phoneNumber(row.getCell(5).getStringCellValue())
                        .imageUrl(row.getCell(6).getStringCellValue())
                        .birthday(row.getCell(7).getLocalDateTimeCellValue().toLocalDate())
                        .email(row.getCell(8).getStringCellValue())
                        .status(row.getCell(9).getBooleanCellValue())
                        .build();
                userList.add(user);
            }
            userService.saveAll(userList); // Lưu tất cả người dùng vào DB
            return new ResponseEntity<>("Import successful!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to import users: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=users.xlsx")
                        .body(bytes);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
