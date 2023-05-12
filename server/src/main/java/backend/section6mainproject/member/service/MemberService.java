package backend.section6mainproject.member.service;

import backend.section6mainproject.member.dto.MemberDTO;
import backend.section6mainproject.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    Long createMember(MemberDTO.PostRequest postRequest);

    MemberDTO.ProfileResponse updateMember(MemberDTO.PatchRequest patchRequest, MultipartFile profileImage);

    Member findVerifiedMember(long memberId); //사용처를 모두 확인한 결과 Service 계층에서만 사용되므로 리턴타입은 Member 엔티티 그대로 두겠음

    void deleteMember(Long memberId);

    MemberDTO.ProfileResponse findMember(Long memberId);
}
