package source.restaurant_web_project.util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Component
public class EmailSender {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.sender}")
    private String sender;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String toEMail,String subject,String body) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(toEMail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        }catch (MessagingException exc){
            throw new RuntimeException(exc);
        }



    }
}
