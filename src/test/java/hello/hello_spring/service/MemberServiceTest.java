package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    // 테스트할 서비스와 레포지토리
    MemberService memberService;
    MemoryMemberRepository memberRepository;

    /**
     * @BeforeEach : 각 테스트 실행 전에 매번 호출됨
     * 목적 : 테스트마다 새로운 객체를 생성하여 독립적인 환경 보장
     *
     * 중요 포인트 : DI (Dipendency Injection)패턴!
     * - 같은 MemoryMemberRepository 인스턴스를 MerberService에 주입
     * - 이렇게 해야 테스트에서 데이터를 clear할 수 있음
     */
    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository(); // 레포지토리 먼저 생성
        memberService = new MemberService(memberRepository); // 같은 인스턴스를 서비스에 주입
        // 이렇게 하면 memberService와 테스트가 같은 repository를 공유
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        //given (준비) : 테스트에 사용할 회원 데이터 생성
        Member member = new Member();
        member.setName("kelly");

        //when (실행) : 실제 테스트할 기능 실행
        Long saveId = memberService.join(member);

        //then (검증) : 결과가 예상대로인지 확인
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("austin");

        Member member2 = new Member();
        member2.setName("austin");

        //when : 첫 번째 회원은 정상 가입
        memberService.join(member1);

        //then : 두 번째 회원 가입 시 예외가 발생해야 함
        //assertThrows(예외클래스, 실행할 코드)
        //member2를 join할 때 IllegalStateException이 발생하는지 확인
        IllegalStateException e = assertThrows(IllegalStateException.class, // 이 예외가 발생해야 함
                () -> memberService.join(member2) // 이 코드 실행 시
        );
        // 예외 메시지도 정확한지 확인
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다");

/**
        위 코드는 아래 try-catch 방식을 간결하게 표현한 것
        try {
            memberService.join(member2);
            fail();
        }   catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다");
        }
        assertThrows가 더 가독성이 좋음
*/
    }

    @Test
    void 전체_회원_조회() {
        // TODO: 전체 회원 조회 테스트 구현
        // 예: 회원 2명 저장 → findMembers() 호출 → 결과 size가 2인지 확인

        //given
        Member member1 = new Member();
        member1.setName("toll");
        memberService.join(member1);

        Member member2 = new Member();
        member2.setName("hust");
        memberService.join(member2);
        //when
        List<Member> members = memberService.findMembers();

        //then
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    void 단일_회원_조회() {
        // TODO: 단일 회원 조회 테스트 구현
        // 예: 회원 1명 저장 → findOne()으로 조회 → 이름이 같은지 확인

        //given
        Member member = new Member();
        member.setName("dean");
        Long savedId = memberService.join(member);

        //when
        Member findMember = memberService.findOne(savedId).get();

        //then
        assertThat(findMember.getName()).isEqualTo("dean");
        //또는 객체 전체 비교
        assertThat(findMember).isEqualTo(member);
    }
}