package backend.section6mainproject.member.service;

import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.entity.Member;

import java.util.List;

public interface MemberService {
    MemberServiceDTO.CreateOutput createMember(MemberServiceDTO.CreateInput createInput);

    MemberServiceDTO.Output updateMember(MemberServiceDTO.UpdateInput updateInput);

    void updateMemberPassword(MemberServiceDTO.UpdatePwInput updatePwInput); //클라이언트 측에선 응답바디로 아무것도 요청하지 않고 단지 상태코드만 요청하셔서 void

    Member findVerifiedMember(Long memberId); //사용처를 모두 확인한 결과 Service 계층에서만 사용되므로 리턴타입은 Member 엔티티 그대로 두겠음

    void deleteMember(Long memberId);

    MemberServiceDTO.Output findMember(Long memberId);

    void getTemporaryPasswordThroughEmail(MemberServiceDTO.FindNewPwInput findNewPwInput) throws InterruptedException;

    Long findRecordingWalkLog(Long memberId);
}
