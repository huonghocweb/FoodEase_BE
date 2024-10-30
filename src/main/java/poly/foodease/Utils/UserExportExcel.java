package poly.foodease.Utils;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import poly.foodease.Model.Entity.User;
import poly.foodease.Service.UserService;

public class UserExportExcel {
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    @Autowired
    UserService userService;
    private List<User> listUser;
   
    public UserExportExcel(List<User> listUser) {
        this.listUser = listUser;
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
         
        createCell(row, 0, "User name", style);      
        createCell(row, 1, "Full name", style);       
        createCell(row, 2, "Pass word", style);    
        createCell(row, 3, "Gender", style);
        createCell(row, 4, "Address", style);
        createCell(row, 5, "Phone number", style);
        createCell(row, 6, "Image", style);
        createCell(row, 7, "Birth day", style);
        createCell(row, 8, "Email", style);
        createCell(row, 9, "Status", style);
         
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
                 
        for (User user : listUser) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, user.getUserName(), style);
            createCell(row, columnCount++, user.getFullName(), style);
            createCell(row, columnCount++, user.getPassword(), style);
            if(user.getGender() == null){
                String GioiTinh = "";
                createCell(row, columnCount++, GioiTinh, style);
            } else if(user.getGender() == true) {
                String GioiTinh = "Male";
                createCell(row, columnCount++, GioiTinh, style);
            } else if(user.getGender() == false) {
                String GioiTinh = "Female";
                createCell(row, columnCount++, GioiTinh, style);
            }  
            createCell(row, columnCount++, user.getAddress(), style);
            createCell(row, columnCount++, user.getPhoneNumber(), style);
            createCell(row, columnCount++, user.getImageUrl(), style);
            
            createCell(row, columnCount++, user.getBirthday(), style);
            createCell(row, columnCount++, user.getEmail(), style);
            
            if(user.getStatus() == true)
            {
            	String tinhTrang= "Open";
                createCell(row, columnCount++, tinhTrang, style);
            }
            else if(user.getStatus()== false) {
            	String tinhTrang = "Close";
            	 createCell(row, columnCount++, tinhTrang, style);
            }
           
            
            
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
   
}
