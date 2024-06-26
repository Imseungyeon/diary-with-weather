package syim.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import syim.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer>{
    //JPA에서 자동으로 생성한 findAll과 같은 쿼리메소드 사용
    List<Diary> findAllByDate(LocalDate date);

    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    //해당 Date에 해당하는 Diary 중 첫 번째 Diary, 즉 쿼리문 limit 역할
    Diary getFirstByDate(LocalDate date);

    //해당 Date에 해당하는 Diary 모두 삭제
    void deleteAllByDate(LocalDate date);
}
