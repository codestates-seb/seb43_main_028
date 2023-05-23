package backend.section6mainproject.walklog.repository;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.member.repository.MemberRepository;
import backend.section6mainproject.walklog.WalkLogStubData;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class WalkLogRepositoryTest {
    @Autowired
    private WalkLogRepository walkLogRepository;
    @Autowired
    private MemberRepository memberRepository;

    private WalkLogStubData stubData;
    @BeforeEach
    public void init() {
        stubData = new WalkLogStubData();
    }

    @Test
    void countByMember_MemberId() {
        // given
        Member savedMember = memberRepository.save(stubData.getMemberForRepo());
        long repeats = 5L;
        for (long i = 0L; i < repeats; i++) {
            walkLogRepository.save(stubData.getWalkLogForRepo(savedMember));
        }

        // when
        Long count = walkLogRepository.countByMember_MemberId(savedMember.getMemberId());

        // then
        MatcherAssert.assertThat(count, Matchers.is(repeats));
    }
}