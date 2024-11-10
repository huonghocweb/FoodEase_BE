package poly.foodease.ServiceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.MailInfo;
import poly.foodease.Service.MailService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class MailServiceImpl implements MailService {

    List<MailInfo> listEmails = new ArrayList<>();

    @Autowired
    // JavaMailSender là interface cung cấp các phương thức để tạo và gửi email nâng cao .
    JavaMailSender javaMailSender;

    @Override
    public void send(MailInfo mail) throws MessagingException {
        // MimeMessage là lớp đại diện cho 1 email có cc tính năng nâng cao như định dạng HTML , đính kèm files
        MimeMessage message = javaMailSender.createMimeMessage();
        // Cung cấp tiện ích cho các thuộc tính của email
        // mesage : đối  tượng đại diện cho email được tạo ở trên
        // true : lựa chọn gửi kèm file, tệp
        // "utf-8" : định dạng hỗ trợ ngôn ngữ
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(mail.getFrom());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        // Cho phép nội dung sử dụng html
        helper.setText(mail.getBody(), true);
        helper.setReplyTo(mail.getFrom());
        String[] cc = mail.getCc();
        if (cc != null && cc.length > 0) {
            helper.setCc(cc);
        }
        String[] bcc = mail.getBcc();
        if (bcc != null && bcc.length > 0) {
            helper.setBcc(bcc);
        }
        List<File> files = mail.getFiles();
        if (files.size() > 0) {
            for (File file : files) {
                helper.addAttachment(file.getName(), file);
            }
        }
        javaMailSender.send(message);
    }

    @Override
    public void send(String to, String subject, String body) throws MessagingException {
        this.send(new MailInfo(to, subject, body));
    }

    @Override
    public void queue(MailInfo mail) {
        listEmails.add(mail);
    }

    @Override
    public void queue(String to, String subject, String body) {
        queue(new MailInfo(to, subject, body));
    }

    @Scheduled(fixedDelay = 100)
    public void run() throws MessagingException {
        while (!listEmails.isEmpty()) {
            MailInfo mail = listEmails.remove(0);
            try {
                this.send(mail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    public void sendResetPasswordEmail(String email, String token) throws MessagingException {
//        String confirmYesUrl = "http://localhost:3000/reset-password-confirm?token=" + token + "&action=yes";
//        String confirmNoUrl = "http://localhost:3000/reset-password-confirm?token=" + token + "&action=no";
//
//        String message = "<p>You requested to reset your password.</p>"
//                + "<p>To confirm, click <a href=\"" + confirmYesUrl + "\">Yes</a>.</p>"
//                + "<p>If this was not you, click <a href=\"" + confirmNoUrl + "\">No</a>.</p>";
//
//        // Cấu hình email gửi đi
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//
//        helper.setTo(email);
//        helper.setSubject("Reset Your Password");
//        helper.setText(message, true); // Kích hoạt HTML
//
//        javaMailSender.send(mimeMessage);
//    }
//    Hòa
@Override
public void sendResetCodeEmail(String email, String code) throws MessagingException {
    String message = "<p>Your password reset code is: <b>" + code + "</b></p>"
            + "<p>This code will expire in 5 minutes.</p>";

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

    helper.setTo(email);
    helper.setSubject("Your Password Reset Code");
    helper.setText(message, true);

    javaMailSender.send(mimeMessage);
}
//    Hòa

}


