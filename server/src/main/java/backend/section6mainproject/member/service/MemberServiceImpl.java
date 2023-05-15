package backend.section6mainproject.member.service;

import backend.section6mainproject.auth.utils.CustomAuthorityUtils;
import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.mapper.MemberMapper;
import backend.section6mainproject.member.repository.MemberRepository;
import backend.section6mainproject.utils.CustomBeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Transactional
@Service
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final StorageService storageService;
    private final CustomBeanUtils<Member> beanUtils;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberMapper mapper;

    public MemberServiceImpl(MemberRepository memberRepository, StorageService storageService, CustomBeanUtils<Member> beanUtils, PasswordEncoder passwordEncoder, CustomAuthorityUtils authorityUtils, MemberMapper mapper) {
        this.memberRepository = memberRepository;
        this.storageService = storageService;
        this.beanUtils = beanUtils;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
        this.mapper = mapper;
    }

    @Override
    public MemberServiceDTO.CreatedToController createMember(MemberServiceDTO.PostInService postInService) {
        //먼저 컨트롤러에서 던져진 서비스계층용 DTO 파라미터 postRequest를 Member 엔티티로 변환한다.
        Member member = mapper.memberPostRequestToMember(postInService);
        //이제 변환된 엔티티 member를 서비스 비즈니스 계층에서 사용해도 된다.
        verifyExistsEmail(member.getEmail());

        encodeMemberCredential(member);
        member.setRoles(authorityUtils.getRoles());

        Member savedMember = memberRepository.save(member);
        MemberServiceDTO.CreatedToController memberIdResponse = mapper.makeMemberIdAfterPostMember(savedMember);

        return memberIdResponse;
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
    public MemberServiceDTO.ProfileResponseForController updateMember(MemberServiceDTO.PatchInService patchInService) {
        //먼저 컨트롤러에서 던져진 서비스계층용 DTO 파라미터 patchRequest를 Member 엔티티로 변환한다.
        Member member = mapper.memberPatchRequestToMember(patchInService);
        //이제 변환된 엔티티 member를 서비스 비즈니스 계층에서 사용해도 된다.
        Member findMember = findVerifiedMember(member.getMemberId());
        //기존 회원의 프로필이미지가 있다면 삭제
        storageService.delete(findMember.getProfileImage());
        encodeMemberCredential(member);

        Member updatedMember = beanUtils.copyNonNullProperties(member, findMember);

        String profile = storageService.store(patchInService.getProfileImage(), "profile");

        updatedMember.setProfileImage(profile);
        memberRepository.save(updatedMember);
        //컨트롤러로 다시 던지기 전에 mapper로 변환해서 응답용DTO를 전달해준다.
        return mapper.memberToMemberResponseDto(updatedMember);
    }

    @Override
    public void deleteMember(Long memberId) {
        Member findMember = findVerifiedMember(memberId);
        findMember.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
        memberRepository.save(findMember);
    }

    @Override
    public MemberServiceDTO.ProfileResponseForController findMember(Long memberId) {
        Member invokedMember = findVerifiedMember(memberId);
        return mapper.memberToMemberResponseDto(invokedMember);
    }

    private void distinguishQuittedMember(Member member) {
        if(member.getMemberStatus().equals(Member.MemberStatus.MEMBER_QUIT)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

}
