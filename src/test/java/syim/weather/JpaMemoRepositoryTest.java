package syim.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import syim.weather.domain.Memo;
import syim.weather.repository.JpaMemoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//DB에 수정사항 만들지 않는 어노테이션 = Transactional
@SpringBootTest
@Transactional
public class JpaMemoRepositoryTest {

    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
    void insertMemoTest(){
        //given 작성 시 무엇을 테스트 하고 싶은 지 확실하게 확인 가능
        //given
        Memo newMemo = new Memo(10, "this is jpa memo");
        //when
        jpaMemoRepository.save(newMemo);
        //then

        List<Memo> memoList = jpaMemoRepository.findAll();
        assertTrue(memoList.size() > 0);
    }

    @Test
    void findByIdTest(){
        //given
        Memo newMemo = new Memo(11, "jpa");
        //when
        Memo memo = jpaMemoRepository.save(newMemo);
        System.out.println(memo.getId());
        //then
        Optional<Memo> result = jpaMemoRepository.findById(memo.getId());
        assertEquals(result.get().getText(), "jpa");
        //mysql에서 키값을 자동으로 생성해주고 있기 때문에(autoimplement) newMemo 만들 때 넣은 id는 의미가 없는 상황
    }
}
