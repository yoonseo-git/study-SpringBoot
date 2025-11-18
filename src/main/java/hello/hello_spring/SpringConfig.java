package hello.hello_spring;

import hello.hello_spring.aop.TimeTraceAop;
import hello.hello_spring.repository.*;
import hello.hello_spring.service.MemberService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        // 여기서 주입되는 것
        // SpringDataJpaMemberRepository의 프록시 구현체
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

    /*
    @Bean
    public MemberRepository memberRepository() {

        return new MemoryMemberRepository(); //1단계 : 메모리
        return new JdbcMemberRepository(dataSource); //2단계 : JDBC
        return new JdbcTemplateMemberRepository(dataSource); //3단계 : JdbcTemplate
        return new JpaMemberRepository(em); //4단계 : JPA
    }
    */

}
