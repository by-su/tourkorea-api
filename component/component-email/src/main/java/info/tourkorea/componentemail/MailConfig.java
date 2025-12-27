package info.tourkorea.componentemail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Value("${email.password}") private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("tourkorea.service@gmail.com");
        javaMailSender.setPassword(password);
        javaMailSender.getJavaMailProperties().put("mail.smtp.auth", true);
        javaMailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", true);
        javaMailSender.getJavaMailProperties().put("mail.smtp.starttls.required", true);
        javaMailSender.getJavaMailProperties().put("mail.smtp.timeout", 5000);
        return javaMailSender;
    }
}