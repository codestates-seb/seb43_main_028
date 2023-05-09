package backend.section6mainproject.member.service;

import backend.section6mainproject.member.entity.Member;

public interface MemberService {
    Member createMember(Member member);

    Member updateMember(Member member);

    Member findVerifiedMember(long memberId);

    void deleteMember(Long memberId);
}
