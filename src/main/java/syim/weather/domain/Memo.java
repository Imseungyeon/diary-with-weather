package syim.weather.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "memo")
public class Memo {

    //mysql에 맞추어 아이디 가져오는 것이 GenerationType.IDENTITY
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;

}
