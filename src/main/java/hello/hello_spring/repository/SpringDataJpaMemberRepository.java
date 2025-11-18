package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
    // JpaRepository가 이미 save, findById, findAll 제공
    @Override
    Optional<Member> findByName(String name); // 커스텀 메서드 이것만 추가
}
