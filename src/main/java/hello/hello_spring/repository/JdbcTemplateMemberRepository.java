package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository {
    // JdbcTemplate : JDBC의 반복적인 코드(Connection, PreparedStatement, ResultSet 처리 등)를
    // 대신 차리해주는 Spring의 핵심 클래스
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(DataSource dataSource) {
        //DataSource를 주입받아서 JdbcTemplate 생성
        // DataSource : 데이터베이스 연결 정보를 담고 있는 객체 (application.properties에서 설정)
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        // SimpleJdbcInsert : INSERT 쿼리를 자동으로 생성해주는 편리한 클래스
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        // INSERT할 테이블 이름과 자동 생성되는 키 컬럼 지정
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        // INSERT할 데이터를 Map에 담기
        // Key : 컬럼명 , value : 실제 값
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        // INSERT 실행 후 자동 생성된 id 값을 받아옴
        // executeAndReturnKey() : INSERT 후 생성된 Primary Key를 반환
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        // 생성된 id를 member 객체에 세팅
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // jdbcTemplate.query(): SELECT 쿼리 실행
        // - 첫 번째 파라미터: SQL 쿼리 (? 는 바인딩 변수)
        // - 두 번째 파라미터: RowMapper (ResultSet을 Member 객체로 변환하는 방법)
        // - 세 번째 파라미터: ? 에 바인딩될 실제 값 (id)
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id);

        // List를 Stream으로 변환 후 첫 번째 요소를 Optional로 반환
        // findAny(): 순서 상관없이 찾은 요소 반환 (보통 첫 번째)
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name);
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    // RowMapper: ResultSet(DB 조회 결과)을 Member 객체로 변환하는 역할
    private RowMapper<Member> memberRowMapper() {
        // 람다식으로 구현
        // rs: ResultSet (DB에서 가져온 한 행의 데이터)
        // rowNum: 현재 처리중인 행 번호 (사용 안 함)
        return (rs, rowNum) -> {
            Member member = new Member();
            // ResultSet에서 컬럼 값을 꺼내서 Member 객체에 세팅
            member.setId(rs.getLong("id")); // id 컬럼 값
            member.setName(rs.getString("name")); // name 컬럼 값
            return member;
        };
    }
}
