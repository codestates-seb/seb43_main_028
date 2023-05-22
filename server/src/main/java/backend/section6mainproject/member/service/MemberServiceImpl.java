package backend.section6mainproject.member.service;

import backend.section6mainproject.auth.utils.CustomAuthorityUtils;
import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.helper.event.PasswordResetEvent;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.member.dto.MemberServiceDTO;
import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.mapper.MemberMapper;
import backend.section6mainproject.member.repository.MemberRepository;
import backend.section6mainproject.utils.CustomBeanUtils;
import backend.section6mainproject.walklog.entity.WalkLog;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Transactional
@Service
public class MemberServiceImpl implements MemberService{
    private int DEFAULT_MEMBER_PASSWORD_LENGTH = 10;
    private final MemberRepository memberRepository;
    private final StorageService storageService;
    private final CustomBeanUtils<Member> beanUtils;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberMapper mapper;
    private final ApplicationEventPublisher publisher;

    public MemberServiceImpl(MemberRepository memberRepository, StorageService storageService, CustomBeanUtils<Member> beanUtils, PasswordEncoder passwordEncoder, CustomAuthorityUtils authorityUtils, MemberMapper mapper, ApplicationEventPublisher publisher) {
        this.memberRepository = memberRepository;
        this.storageService = storageService;
        this.beanUtils = beanUtils;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    @Override
    public MemberServiceDTO.CreateOutput createMember(MemberServiceDTO.CreateInput createInput) {
        //먼저 컨트롤러에서 던져진 서비스계층용 DTO 파라미터 postRequest를 Member 엔티티로 변환한다.
        Member member = mapper.createInputToMember(createInput);
        //이제 변환된 엔티티 member를 서비스 비즈니스 계층에서 사용해도 된다.
        verifyExistsEmail(member.getEmail());
        verifyExistsNickname(member.getNickname());

        encodeMemberCredential(member);
        member.setRoles(authorityUtils.getRoles());

        Member savedMember = memberRepository.save(member);
        MemberServiceDTO.CreateOutput memberIdResponse = mapper.memberToCreateOutput(savedMember);

        return memberIdResponse;
    }

    private void encodeMemberCredential(Member member) {
        if(member.getPassword() == null) return;
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);
    }

    private void verifyExistsEmail(String email) {
       Optional<Member> findMember = memberRepository.findByEmail(email);
       if(findMember.isPresent()) {
           throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
       }
    }

    private void verifyExistsNickname(String nickname) {
        Optional<Member> findMember = memberRepository.findByNickname(nickname);
        if(findMember.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }


    @Override
    public Member findVerifiedMember(Long memberId) {
        Optional<Member> optionalMember =
                memberRepository.findById(memberId);
        Member findMember =
                optionalMember.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        distinguishQuittedMember(findMember);
        return findMember;
    }
    @Override
    public MemberServiceDTO.Output updateMember(MemberServiceDTO.UpdateInput updateInput) {
        if(updateInput.getNickname() != null) {
            verifyExistsNickname(updateInput.getNickname());
        }
        //먼저 컨트롤러에서 던져진 서비스계층용 DTO 파라미터 patchRequest를 Member 엔티티로 변환한다.
        Member member = mapper.updateInputToMember(updateInput);
        //이제 변환된 엔티티 member를 서비스 비즈니스 계층에서 사용해도 된다.
        Member findMember = findVerifiedMember(member.getMemberId());
        Member updatedMember = beanUtils.copyNonNullProperties(member, findMember);
        String profile = storageService.store(updateInput.getProfileImage(), "profile");
        if(profile != null || storageService.isEmptyFile(updateInput.getProfileImage())) {
            storageService.delete(findMember.getProfileImage());
            updatedMember.setProfileImage(profile);
        }
        memberRepository.save(updatedMember);
        //컨트롤러로 다시 던지기 전에 mapper로 변환해서 응답용DTO를 전달해준다.
        return mapper.memberToOutput(updatedMember);
    }
    @Override
    public void updateMemberPassword(MemberServiceDTO.UpdatePwInput updatePwInput) {
        Long memberId = updatePwInput.getMemberId();
       Member findMember = findVerifiedMember(memberId); // 등록된 적 없는 회원이면 에러뜬다.
        encodeMemberCredentialForUpdatePw(updatePwInput, findMember);

        memberRepository.save(findMember);
    }

    @Override
    public void getTemporaryPasswordThroughEmail(MemberServiceDTO.FindNewPwInput findNewPwInput) throws InterruptedException {
        //먼저 요청으로 들어온 이메일을 통한 회원이 존재하는지 확인한다.
        Member verifiedMember = findVerifiedMemberWithEmail(findNewPwInput.getEmail());

        //임시비밀번호를 생성한다.
        String tmpPw = getTempPassword(DEFAULT_MEMBER_PASSWORD_LENGTH);

        //비밀번호를 수정한다.
        String encodedTmpPw = passwordEncoder.encode(tmpPw);
        verifiedMember.setPassword(encodedTmpPw);
        Member memberWithChangedPw = memberRepository.save(verifiedMember);

        //이제 메일을 보낸다.
        publisher.publishEvent(new PasswordResetEvent(this, memberWithChangedPw, tmpPw));
    }

    private Member findVerifiedMemberWithEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        Member findMember =
                member.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findMember;
    }

    private void encodeMemberCredentialForUpdatePw(MemberServiceDTO.UpdatePwInput updatePwInput, Member findMember) {
        String encodedPassword = passwordEncoder.encode(updatePwInput.getPassword());
        findMember.setPassword(encodedPassword); //기존 encodeMemberCredential() 메소드의 경우 createMember()와 결합이 강해서 새로운 메소드를 만들었음
    }

    @Override
    public void deleteMember(Long memberId) {
        Member findMember = findVerifiedMember(memberId);
        findMember.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
        memberRepository.save(findMember);
    }

    @Override
    public MemberServiceDTO.Output findMember(Long memberId) {
        Member invokedMember = findVerifiedMember(memberId);
        return mapper.memberToOutput(invokedMember);
    }

    @Override
    public Long findRecordingWalkLog(Long memberId) {
        Member findMember = findVerifiedMember(memberId);
        List<WalkLog> walkLogs = findMember.getWalkLogs();
        Long walkLogId = null;
        for (int i = walkLogs.size() - 1; i >= 0; i--) {
            if(walkLogs.get(i).getWalkLogStatus() == WalkLog.WalkLogStatus.RECORDING) {
                walkLogId = walkLogs.get(i).getWalkLogId();
                break;
            }
        }
        if(walkLogId == null) throw new BusinessLogicException(ExceptionCode.WALK_LOG_NOT_FOUND);
        return walkLogId;
    }

    private void distinguishQuittedMember(Member member) {
        if(member.getMemberStatus().equals(Member.MemberStatus.MEMBER_QUIT)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    private String getTempPassword(int size) {
    char[] charSet = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '!', '@', '#', '$', '%', '^', '&' };

    StringBuffer sb = new StringBuffer();
    SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());

    int idx = 0;
    int len = charSet.length;
        for (int i=0; i<size; i++) {
        // idx = (int) (len * Math.random());
        idx = sr.nextInt(len);    // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
        sb.append(charSet[idx]);
    }
        return sb.toString();
    }
}