package syim.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import syim.weather.domain.Memo;

//Spring에게 이게 레파지토리임을 알려주기 위한 어노테이션 - Repository
//JPA는 Java의 표준 ORM 명제 따라서 자바에서 ORM 개념을 활용할 때 쓸 함수들을 JpaRepository에 이미 정리가 되어있음
//어떠한 함수를 구현한 적이 없음에도 save나 findAll같은 함수들이 이미 JpaRepository에 있기 때문에 사용 가능

@Repository
public interface JpaMemoRepository extends JpaRepository<Memo, Integer> {

}
