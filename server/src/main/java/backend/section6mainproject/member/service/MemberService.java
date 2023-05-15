package backend.section6mainproject.member.service;

import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.entity.Member;

public interface MemberService {
    MemberServiceDTO.CreatedToController createMember(MemberServiceDTO.PostInService postInService);

    MemberServiceDTO.ProfileResponseForController updateMember(MemberServiceDTO.PatchInService patchInService);

    Member findVerifiedMember(long memberId); //사용처를 모두 확인한 결과 Service 계층에서만 사용되므로 리턴타입은 Member 엔티티 그대로 두겠음

    void deleteMember(Long memberId);

    MemberServiceDTO.ProfileResponseForController findMember(Long memberId);
}
