package syim.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    //조회할 때는 @GetMapping 사용하고 저장할 때는 Post, 수정할 때는 Put
    //""안의 create/diary라는 path로 요청을 보내면 함수가 동작
    //@RequestParam으로 인해 /create/diary?date = 20230611과 같은 형식으로 뒤에 파라미터로 보낼 수 있게 되는 것
    //날짜 형식이 제각각이기 때문에 @DateTimeFormat으로 형식 지정
    //Body값으로 String 형식의 일기를 넣을 것
    @Operation(summary = "날짜와 일기 텍스트를 DB에 저장합니다.", description = "파라미터로 받은 날짜에 대한 날씨와 함께 일기를 생성하여 저장합니다.")
    @PostMapping("/create/diary")
    void createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(name = "date", example = "yyyy-MM-dd") LocalDate date, @RequestBody String text){
        diaryService.createDiary(date, text);
    }

    //일기를 날짜에 따라 조회
    @Operation(summary = "선택한 날짜의 모든 날씨 + 일기 데이터를 가져옵니다.", description = "파라미터로 받은 날짜에 해당하는 날씨와 일기를 조회합니다.")
    @GetMapping("/read/diary")
    List<Diary> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(name = "date", example = "yyyy-MM-dd") LocalDate date){
        return diaryService.readDiary(date);
    }

    @Operation(summary = "선택한 기간의 모든 날씨 + 일기 데이터를 가져옵니다.", description = "파라미터로 받은 기간에 해당하는 모든 날씨와 일기를 조회합니다.")
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(name = "startDate", description = "기간의 첫 번째 날", example = "yyyy-MM-dd") LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(name = "endDate", description = "기간의 마지막 날", example = "yyyy-MM-dd") LocalDate endDate){
        return diaryService.readDiaries(startDate, endDate);
    }

    @Operation(summary = "선택한 날짜의 일기 텍스트를 수정합니다.", description = "파라미터로 받은 날짜에 대한 일기를 수정하여 저장합니다.")
    @PutMapping("/update/diary")
    void updateDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(name = "date", example = "yyyy-MM-dd") LocalDate date, @RequestBody String text){
        diaryService.updateDiary(date, text);
    }

    @Operation(summary = "선택한 날짜의 일기를 삭제합니다.", description = "파라미터로 받은 날짜에 대한 일기를 삭제합니다.")
    @DeleteMapping("/delete/diary")
    void deleteDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(name = "date", example = "yyyy-MM-dd") LocalDate date){
        diaryService.deleteDiary(date);
    }
}
