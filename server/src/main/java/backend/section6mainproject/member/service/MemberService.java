package backend.section6mainproject.member.service;

import backend.section6mainproject.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    Member createMember(Member member);

    Member updateMember(Member member, MultipartFile profileImage);

    Member findVerifiedMember(long memberId);

    void deleteMember(Long memberId);

    Member findMember(Long memberId);
}
