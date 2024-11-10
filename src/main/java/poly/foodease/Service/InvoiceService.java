package poly.foodease.Service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.Order;
import poly.foodease.Model.Entity.OrderDetails;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Stream;


@Service
public class InvoiceService {

    public ByteArrayInputStream generateInvoicePdf(Order order) throws IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // Khởi tạo PdfWriter
            PdfWriter.getInstance(document, out);
            document.open();

            // Cài đặt font chữ
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

            // Thêm tiêu đề
            Paragraph title = new Paragraph("HÓA ĐƠN MUA HÀNG", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" ")); // Dòng trống

            // Thông tin khách hàng
            document.add(new Paragraph("Tên khách hàng: " + order.getUser().getFullName(), normalFont));
            document.add(new Paragraph("Địa chỉ: " + order.getDeleveryAddress(), normalFont));
            document.add(new Paragraph("Ngày mua: " + order.getOrderDate().toString(), normalFont));

            document.add(new Paragraph(" ")); // Dòng trống

            // Bảng chi tiết đơn hàng
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 2, 2, 2});

            // Header của bảng
            addTableHeader(table, headerFont);

            // Nội dung bảng
            addRows(table, order, normalFont);

            document.add(table);

            document.add(new Paragraph(" ")); // Dòng trống

            // Tổng tiền
            Paragraph total = new Paragraph("Tổng tiền: " + String.format("%.2f", order.getTotalPrice()) + " VND", headerFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();

        } catch (DocumentException ex) {
            throw new IOException(ex.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table, Font font) {
        Stream.of("Sản phẩm", "Đơn giá", "Số lượng", "Thành tiền")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(Color.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle, font));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, Order order, Font font) {
        for (OrderDetails detail : order.getOrderDetails()) {
            // Tên sản phẩm
            table.addCell(new PdfPCell(new Phrase(
                    detail.getFoodVariations().getFood().getFoodName(), font)));

            // Đơn giá
            table.addCell(new PdfPCell(new Phrase(
                    String.format("%.2f", detail.getPrice()), font)));

            // Số lượng
            table.addCell(new PdfPCell(new Phrase(
                    String.valueOf(detail.getQuantity()), font)));

            // Thành tiền
            double totalPrice = detail.getPrice() * detail.getQuantity();
            table.addCell(new PdfPCell(new Phrase(
                    String.format("%.2f", totalPrice), font)));
        }
    }

}
