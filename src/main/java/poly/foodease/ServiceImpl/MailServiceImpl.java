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
        MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
        helper.setFrom(mail.getFrom());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        // Cho phép nội dung sử dụng html
        helper.setText(mail.getBody(),true);
        helper.setReplyTo(mail.getFrom());
        String [] cc = mail.getCc();
        if(cc != null &&  cc.length > 0) {
            helper.setCc(cc);
        }
        String [] bcc = mail.getBcc();
        if(bcc != null &&  bcc.length > 0) {
            helper.setBcc(bcc);
        }
        List<File> files = mail.getFiles();
        if(files.size() > 0){
            for(File file : files){
                helper.addAttachment(file.getName(), file);
            }
        }
        javaMailSender.send(message);
    }

    @Override
    public void send(String to, String subject, String body) throws MessagingException {
        this.send(new MailInfo(to,subject,body));
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
        while (!listEmails.isEmpty()){
            MailInfo mail = listEmails.remove(0);
            try {
                this.send(mail);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

