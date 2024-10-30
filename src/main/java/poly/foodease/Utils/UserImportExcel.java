package poly.foodease.Utils;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import poly.foodease.Model.Entity.User;
import poly.foodease.Service.UserService;

public class UserImportExcel {
	@Autowired
	UserService userService;

	public UserImportExcel(UserService userService) {
		this.userService = userService;
	}

	public void importExcelData(String filePath) {
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

				// Handle birthday
				if (row.getCell(7).getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(row.getCell(7))) {
					Date date = row.getCell(7).getDateCellValue();
					LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					user.setBirthday(localDate);
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
}
