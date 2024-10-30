package poly.foodease.Controller.Api;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Request.UserRequest;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Report.ReportUserBuy;
import poly.foodease.Service.UserService;
import poly.foodease.Utils.UserBuyExportExcel;
import poly.foodease.Utils.UserExportExcel;
import poly.foodease.Utils.UserImportExcel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
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
	public ResponseEntity<Object> getUserByUserName(@PathVariable("userName") String userName)
			throws JsonProcessingException {
		Map<String, Object> result = new HashMap<>();
		try {
			result.put("success", true);
			result.put("message", "get User By UserName");
			result.put("data", userService.getUserByUsername(userName));
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
			result.put("data", null);
		}
		return ResponseEntity.ok(result);
	}

	@GetMapping
	public ResponseEntity<Page<UserResponse>> getUsers(@RequestParam(defaultValue = "0") Integer pageNumber,
			@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "asc") String sortOrder,
			@RequestParam(defaultValue = "userName") String sortBy) {

		Page<UserResponse> usersPage = userService.getAllUserByPage(pageNumber, pageSize, sortOrder, sortBy);

		if (usersPage.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Trả về 204 nếu không có người dùng
		}
		return ResponseEntity.ok(usersPage); // Trả về 200 và danh sách người dùng
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
		return userService.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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
		return userService.updateUser(userRequest, id).map(ResponseEntity::ok)
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
				User user = User.builder().userName(row.getCell(0).getStringCellValue())
						.fullName(row.getCell(1).getStringCellValue()).password(row.getCell(2).getStringCellValue())
						.gender(row.getCell(3).getBooleanCellValue()).address(row.getCell(4).getStringCellValue())
						.phoneNumber(row.getCell(5).getStringCellValue()).imageUrl(row.getCell(6).getStringCellValue())
						.birthday(row.getCell(7).getLocalDateTimeCellValue().toLocalDate())
						.email(row.getCell(8).getStringCellValue()).status(row.getCell(9).getBooleanCellValue())
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
				return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=users.xlsx").body(bytes);
			}
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ngoc
	private void importExcelData(String filePath) {
		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue; // Skip the header row

				User user = new User();

				// Read userName
				user.setUserName(row.getCell(0).getStringCellValue());

				// Read fullName
				user.setFullName(row.getCell(1).getStringCellValue());

				// Read password with type checking
				if (row.getCell(2).getCellType() == CellType.NUMERIC) {
					int pass = (int) row.getCell(2).getNumericCellValue();

					user.setPassword(String.valueOf(pass));
				} else {
					user.setPassword(row.getCell(2).getStringCellValue());
				}

				// Read gender
				if (row.getCell(3).getCellType() == CellType.STRING) {
					String genderValue = row.getCell(3).getStringCellValue().toLowerCase();
					user.setGender(genderValue.equals("male") || genderValue.equals("female"));
				}

				// Read address
				user.setAddress(row.getCell(4).getStringCellValue());

				// Read phone number with type checking
				if (row.getCell(5).getCellType() == CellType.NUMERIC) {
					int SDT = (int) row.getCell(5).getNumericCellValue();
					user.setPhoneNumber(String.valueOf(SDT));
				} else {
					user.setPhoneNumber(row.getCell(5).getStringCellValue());
				}

				// Read image URL
				user.setImageUrl(row.getCell(6).getStringCellValue());

				if (row.getCell(7).getCellType() == CellType.NUMERIC) {
					Cell cell = row.getCell(7);
					if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
						System.out.println("ô bảy");

						// Lấy giá trị ngày từ ô
						Date date = cell.getDateCellValue();
						LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

						System.out.println(localDate);
						user.setBirthday(localDate);// In ra giá trị LocalDate
					}
				}
				// Read email
				user.setEmail(row.getCell(8).getStringCellValue());

				// Read status with type checking
				if (row.getCell(9).getCellType() == CellType.NUMERIC) {
					int statusValue = (int) row.getCell(9).getNumericCellValue();
					user.setStatus(statusValue != 0);
				}

				// Save user
				userService.SaveUser(user);
			}

		} catch (Exception e) {
			e.printStackTrace(); // Handle error appropriately
		}
	}

	 @PostMapping("/importUser")
	    public ResponseEntity<String> importUserBuy(@RequestParam("file") MultipartFile file) {
	        if (file == null || file.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tệp không hợp lệ.");
	        }

	        String directoryPath = "E:\\DuAnTotNghiep\\Code\\Be-24-10\\file\\";
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
