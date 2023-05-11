package backend.section6mainproject.member.service;

import backend.section6mainproject.auth.utils.CustomAuthorityUtils;
import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.repository.MemberRepository;
import backend.section6mainproject.utils.CustomBeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
@Transactional
@Service
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final StorageService storageService;
    private final CustomBeanUtils<Member> beanUtils;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    public MemberServiceImpl(MemberRepository memberRepository, StorageService storageService, CustomBeanUtils<Member> beanUtils, PasswordEncoder passwordEncoder, CustomAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.storageService = storageService;
        this.beanUtils = beanUtils;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
    }

    @Override
    public Long createMember(Member member) {
        verifyExistsEmail(member.getEmail());

        encodeMemberCredential(member);
        member.setRoles(authorityUtils.getRoles());

        Member savedMember = memberRepository.save(member);
        return savedMember.getMemberId();
    }

    private void encodeMemberCredential(Member member) {
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);
    }

    private void verifyExistsEmail(String email) {
       Optional<Member> findMember = memberRepository.findByEmail(email);
       if(findMember.isPresent()) {
           throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
       }
    }

    /** 회원이 리포지토리에 존재하는지 여부를 검증하고, 회원을 반환하는 메서드.
     * <p>Member 엔티티의 walkLogs 필드는 fetchType이 Lazy로 설정되어 있으므로, 값을 실제로 조회할 때 데이터를 호출하게 되는데,
     *  ConnectionInterceptor가 영속성 컨텍스트 밖에 있는것으로 보여서 이 메서드에서 값을 호출했다.
     */
    @Override
    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember =
                memberRepository.findById(memberId);
        Member findMember =
                optionalMember.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        findMember.getWalkLogs().stream().forEach(walkLog -> walkLog.getWalkLogId());
        distinguishQuittedMember(findMember);
        return findMember;
    }
    @Override
    public Member updateMember(Member member, MultipartFile profileImage) {
        Member findMember = findVerifiedMember(member.getMemberId());
        //기존 회원의 프로필이미지가 있다면 삭제
        storageService.delete(findMember.getProfileImage());
        encodeMemberCredential(member);

        Member updatedMember = beanUtils.copyNonNullProperties(member, findMember);

        String profile = storageService.store(profileImage, "profile");

        updatedMember.setProfileImage(profile);

        return memberRepository.save(updatedMember);
    }

    @Override
    public void deleteMember(Long memberId) {
        Member findMember = findVerifiedMember(memberId);
        findMember.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
        memberRepository.save(findMember);
    }

    @Override
    public Member findMember(Long memberId) {
        Member member = findVerifiedMember(memberId);
        return member;
    }

    private void distinguishQuittedMember(Member member) {
        if(member.getMemberStatus().equals(Member.MemberStatus.MEMBER_QUIT)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

}
