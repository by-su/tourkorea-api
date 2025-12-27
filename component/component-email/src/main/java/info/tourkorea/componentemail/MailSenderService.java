package info.tourkorea.componentemail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${mail.client.url}") private String serverUrl;

    public void sendVerificationEmail(String email, String title, String description) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(title);

            Context context = new Context();

            String html = templateEngine.process("EmailVerification.html", context);
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPasswordResetEmail(String email, String link) {
        var message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setSubject("Tour Korea Password Reset");
            helper.setTo(email);
            var context = new Context();
            context.setVariable("link", serverUrl + "/account/reset-password?link=" + link);
            var html = templateEngine.process("PasswordEmailForm.html", context);
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
