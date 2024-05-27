package syim.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import syim.weather.domain.Memo;
import syim.weather.repository.JdbcMemoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//테스트 때문에 데이터베이스 안 정보 변경되는 것을 막기 위한 어노테이션 따라서 결과까지 보고싶으면 Transactional 제거
@SpringBootTest
@Transactional

public class JdbcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest(){

        //given
        Memo newMemo = new Memo(2, "insertMemoTest");

        //when
        jdbcMemoRepository.save(newMemo);

        //then
        Optional<Memo> result = jdbcMemoRepository.findById(2);
        assertEquals(result.get().getText(), "insertMemoTest");
    }

    @Test
    void findAllMemoTest(){
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println(memoList);
        assertNotNull(memoList);
    }
}
