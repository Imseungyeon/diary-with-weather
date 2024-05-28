package syim.weather.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import syim.weather.domain.Diary;
import syim.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

//@RestController는 기본 @Controller에 상태코드(200, 404)를 지정해서 내려줄 수 있게함
@RestController
public class DiaryController {
    //컨트롤러는 서비스에 전달을 해주어야 하기 때문에
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    //조회할 때는 @GetMapping 사용하고 저장할 때는 Post를 많이 사용함
    //""안의 create/diary라는 path로 요청을 보내면 함수가 동작
    //@RequestParam으로 인해 /create/diary?date = 20230611과 같은 형식으로 뒤에 파라미터로 보낼 수 있게 되는 것
    //날짜 형식이 제각각이기 때문에 @DateTimeFormat으로 형식 지정
    //Body값으로 String 형식의 일기를 넣을 것
    @PostMapping("/create/diary")
    void createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestBody String text){
        diaryService.createDiary(date, text);
    }

    //일기를 날짜에 따라 조회
    @GetMapping("/read/diary")
    List<Diary> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return diaryService.readDiary(date);
    }
}
