package backend.section6mainproject.helper.email;

public class MockEmailSendable implements EmailSendable{
    @Override
    public void send(String[] to, String subject, String templateName) throws InterruptedException {
        System.out.println("Sent mock email!");
    }
}
