package backend.section6mainproject.helper.event;

import backend.section6mainproject.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class PasswordResetEvent extends ApplicationEvent {
    private Member member;
    private String tmpPw;
    public PasswordResetEvent(Object source, Member member, String tmpPw) {
        super(source);
        this.member = member;
        this.tmpPw = tmpPw;
    }
}
