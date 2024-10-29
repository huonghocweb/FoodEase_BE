package poly.foodease.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import poly.foodease.Model.Entity.User;
import poly.foodease.Report.ReportUserBuy;
import poly.foodease.Service.UserService;

public class UserBuyExportExcel {
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    @Autowired
    UserService userService;
    private List<ReportUserBuy> listUserBuy;
   
    public UserBuyExportExcel(List<ReportUserBuy> listUserBuy) {
        this.listUserBuy = listUserBuy;
        workbook = new XSSFWorkbook();
    }
 
 
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Accounts");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Order date", style);      
        createCell(row, 1, "Full name", style);       
        createCell(row, 2, "Gender", style);    
        createCell(row, 3, "Phone number", style);
        createCell(row, 4, "Email", style);
        createCell(row, 5, "Birthday", style);
        createCell(row, 6, "Address", style);
        createCell(row, 7, "Total Quantity", style);
        createCell(row, 8, "Total price", style);
        
         
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value == null) {
            cell.setCellValue(""); // Set an empty string for null values, adjust as per your requirement
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
        	
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }
        else if(value instanceof Long) {
        	cell.setCellValue((Long) value);
        }
        else {
            cell.setCellValue(String.valueOf(value)); // Use String.valueOf to handle non-standard types
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (ReportUserBuy userBuy : listUserBuy) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, userBuy.getDate(), style);
            createCell(row, columnCount++, userBuy.getFullName(), style);
            if(userBuy.getGender() == null){
                String chucVu = "";
                createCell(row, columnCount++, chucVu, style);
            } else if(userBuy.getGender() == true) {
                String chucVu = "Male";
                createCell(row, columnCount++, chucVu, style);
            } else if(userBuy.getGender() == false) {
                String chucVu = "Female";
                createCell(row, columnCount++, chucVu, style);
            }  
            createCell(row, columnCount++, userBuy.getPhoneNumber(), style);
            createCell(row, columnCount++, userBuy.getEmail(), style);
            createCell(row, columnCount++, userBuy.getBirthday(), style);
            createCell(row, columnCount++, userBuy.getAddress(), style);
            createCell(row, columnCount++, userBuy.getTotalQuantity(), style);
            createCell(row, columnCount++, userBuy.getTotalPrice(), style);
            
            
            
           
           
           
            
             
        }
    }
     
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
    }
    private void importExcelData(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Lấy trang đầu tiên
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Bỏ qua hàng đầu tiên (tiêu đề)

                User user = new User();
                user.setUserName(row.getCell(0).getStringCellValue());
                user.setFullName(row.getCell(1).getStringCellValue());
                user.setPassword(row.getCell(2).getStringCellValue());
                String genderValue = row.getCell(3).getStringCellValue().toLowerCase();
                user.setGender(genderValue.equals("Male") || genderValue.equals("Female"));
                user.setAddress(row.getCell(4).getStringCellValue());
                user.setPhoneNumber(row.getCell(5).getStringCellValue());
                user.setImageUrl(row.getCell(6).getStringCellValue());
                String birthdayString = row.getCell(7).getStringCellValue();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Đảm bảo định dạng khớp với dữ liệu của bạn
                LocalDate birthdayDate = LocalDate.parse(birthdayString, dateFormatter);
                user.setBirthday(birthdayDate);
                user.setEmail(row.getCell(8).getStringCellValue());
                int statusValue = (int) row.getCell(9).getNumericCellValue(); // Lấy giá trị ô và ép kiểu nếu cần
                user.setStatus(statusValue != 0);
              
              
                userService.SaveUser(user); // Lưu vào cơ sở dữ liệu
            }
        } catch (Exception e) {
            e.printStackTrace(); // Bắt lỗi và xử lý hợp lý
        }
    }
}
