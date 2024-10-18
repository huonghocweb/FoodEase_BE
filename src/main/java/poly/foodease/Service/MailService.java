package poly.foodease.Service;

import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.MailInfo;

@Service
public interface MailService {
    void send(MailInfo mail) throws MessagingException, jakarta.mail.MessagingException;
    void send(String to, String subject , String body) throws MessagingException, jakarta.mail.MessagingException;
    void queue(MailInfo mail);
    void queue(String to, String subject, String body);
}
