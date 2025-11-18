package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{

    // EntityManager: JPA의 핵심 인터페이스
    // - 엔티티(Entity)를 관리하고 DB와 통신하는 역할
    // - SQL을 직접 작성하지 않고 객체를 통해 DB 작업 수행
    // - Spring Boot가 자동으로 생성해서 주입해줌
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        // EntityManager를 생성자 주입으로 받음
        // Spring Boot가 application.properties의 DB 설정을 보고 자동 생성
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        // persist(): 엔티티를 영속성 컨텍스트에 저장
        // - JPA가 자동으로 INSERT 쿼리 생성 및 실행
        // - ID가 자동 생성(@GeneratedValue)되면 member에 ID 자동 세팅
        // - SQL: INSERT INTO member (name) VALUES (?)
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // find(): Primary Key(PK)로 엔티티 조회
        // - 첫 번째 파라미터: 조회할 엔티티 클래스
        // - 두 번째 파라미터: PK 값
        // - SQL: SELECT * FROM member WHERE id = ?
        // - 결과가 없으면 null 반환
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        // createQuery(): JPQL(Java Persistence Query Language) 쿼리 실행
        // JPQL: 테이블이 아닌 '엔티티 객체'를 대상으로 쿼리 작성
        // - "Member m": Member 엔티티를 m이라는 별칭으로 사용
        // - "m.name": Member 객체의 name 필드 (DB의 name 컬럼과 매핑됨)
        // - ":name": 파라미터 바인딩 (JDBC의 ? 와 유사하지만 이름 기반)
        List<Member> result = em.createQuery("select m from Member m where m.name= :name", // JPQL 쿼리
                Member.class)   // 반환 타입
                .setParameter("name", name) // :name에 실제 값 바인딩
                .getResultList(); // List로 결과 받기
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        // 모든 Member 조회
        // JPQL: "select m from Member m"
        // - SQL로 변환되면: SELECT * FROM member
        // - Member는 테이블명이 아닌 '엔티티 클래스명'
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
