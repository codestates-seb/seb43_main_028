package backend.section6mainproject.helper.email;

import org.springframework.stereotype.Component;

@Component
public interface EmailSendable {
    void send(String[] to, String subject, String message, String template) throws InterruptedException;
}
