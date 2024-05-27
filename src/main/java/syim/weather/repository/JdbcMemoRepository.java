package syim.weather.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import syim.weather.domain.Memo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

//Jdbc는 Jpa와는 다르게 쿼리문을 일일히 다 작성해주어야 함(memoRowMapper 포함)

@Repository
public class JdbcMemoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcMemoRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Memo save(Memo memo) {
        String sql = "insert into memo values(?,?)";
        jdbcTemplate.update(sql, memo.getId(), memo.getText());
        return memo;
    }

    public List<Memo> findAll() {
        String sql = "select * from memo";
        return jdbcTemplate.query(sql, memoRowMapper());
    }

    public Optional<Memo> findById(int id) {
        String sql = "select * from memo where id = ?";
        return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
    }
    //혹시 모를 NULL값이 처리되는 부분 -> Optional로 변경된 이유

    //맵핑해주는 함수
    private RowMapper<Memo> memoRowMapper() {
        // ResultSet
        // {id = 1, text = 'this is memo'}
        return (rs, rowNum) -> new Memo(
                rs.getInt("id"),
                rs.getString("text")
        );
    }
}
