package backend.section6mainproject.helper.event;

import backend.section6mainproject.helper.email.EmailSender;
import backend.section6mainproject.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@EnableAsync
@Configuration
@Component
@Slf4j
public class PasswordResetEventListner {
    @Value("${mail.subject.member.reset-password}")
    private String subject;
    @Value("${mail.template.name.member.reset}")
    private String templateName;

    private final EmailSender emailSender;

    public PasswordResetEventListner(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    String htmlFilePath = "src/main/resources/templates/email-password-reset.html";

    @Async
    @EventListener
    public void listen(PasswordResetEvent event) throws Exception {
        try {
            String[] to = new String[]{event.getMember().getEmail()};
            String message = event.getMember().getNickname() + "님의 임시 비밀번호 입니다. " + event.getTmpPw();

            emailSender.sendEmail(to, subject, message, templateName);
        } catch (MailSendException e) {
            e.printStackTrace();
            log.error("MailSendException: Password reset Fail:");
        }
    }
}