package poly.foodease.Service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.MailInfo;

@Service
public interface MailService {
    void send(MailInfo mail) throws MessagingException;
    void send(String to, String subject, String body) throws MessagingException;
    void queue(MailInfo mail);

    // Chỉ khai báo jakarta.mail.MessagingException
//    void sendResetPasswordEmail(String email, String token) throws MessagingException;
    void sendResetCodeEmail(String email, String code) throws MessagingException;
    void queue(String to, String subject, String body);
}
