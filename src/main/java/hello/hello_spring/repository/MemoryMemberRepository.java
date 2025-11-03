package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values() //Member들만 꺼냄
                .stream() //스트림으로 변환 (반복 처리 가능하게)
                .filter(member -> member.getName().equals(name)) //조건에 맞는 것만 걸러냄(필터링)
                .findAny(); //필터링 결과 중 아무거나 하나 찾음
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}
