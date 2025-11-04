package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {

    // 테스트할 Repository 객체 생성
    MemoryMemberRepository repository = new MemoryMemberRepository();

    /**
    * @AfterEach : 각 테스트 메서드가 끝날 때마다 실행됨
    * 목적 : 테스트 간 데이터 격리 (한 테스트가 다른 테스트에 영향 주지 않게)
    * 예 : test1에서 저장한 데이터가 test2에 영향 주는 것을 방지
    */
    @AfterEach
    public void afterEach() {
        repository.clearStore(); // 저장소 비우기
    }

    /**
    * 회원 저장 기능 테스트
    * 검증 : 저장한 회원을 ID로 다시 조회했을 때 같은 객체인지 확인
    */
    @Test
    public void save() {
        // 테스트 데이터 준비
        Member member = new Member();
        member.setName("spring");

        // 테스트할 동작 실행
        repository.save(member); // 회원 저장
        Member result = repository.findById(member.getId()).get(); // 저장된 회원 조회
        //Assertions.assertEquals(member, result); JUnit 방식 (구식)
        assertThat(member).isEqualTo(result); //AssertJ 방식 (가독성 좋음)
        // "저장한 member와 조회한 result가 같은지 확인"
    }

    /**
     * 이름으로 회원 찾기 기능 테스트
     * 검증: 이름으로 회원을 조회했을 때 올바른 회원이 반환되는지 확인
     */
    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get(); // Optional이므로 .get()으로 실제 Member 객체 꺼냄
        assertThat(result).isEqualTo(member1);
    }

    /**
     * 전체 회원 조회 기능 테스트
     * 검증: 저장한 회원 수만큼 조회되는지 확인
     */
    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();
        // "저장한 회원이 2명이니까, 조회 결과도 2개여야 함"
        assertThat(result.size()).isEqualTo(2);
    }

}
