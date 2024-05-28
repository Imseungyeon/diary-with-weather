package syim.weather.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import syim.weather.domain.Diary;
import syim.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DiaryService {

    //@Value를 통해 SpringBoot에 이미 지정되어 있는 변수 openweathermap.key의 값을 가져와서 apiKey에 넣어줄 것
    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;

    //DiaryService 빈이 생성될 시 DiaryRepository 가져옴
    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text){
        //기능1. openweathermap에서 날씨 데이터 가져오기
        String weatherData = getWeatherString();

        //기능2. 받아온 날씨 json 파싱
        Map<String, Object> parsedWeather = parseWeather(weatherData);

        //기능3. 파싱된 데이터, 일기 DB에 넣기
        Diary nowDiary = new Diary(); //Diary에 @NoArgsConstructor 어노테이션이 있기에 빈 다이어리 생성 가능
        nowDiary.setWeather(parsedWeather.get("main").toString());
        nowDiary.setIcon(parsedWeather.get("icon").toString());
        nowDiary.setTemperature((double)parsedWeather.get("temp"));
        nowDiary.setText(text);
        nowDiary.setDate(date);
        //nowDiary를 DiaryRepository를 통해 DB에 save
        diaryRepository.save(nowDiary);
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date){
        //diary를 가져오려면 db를 조회해야 하므로 diaryRepository 통하여
        return diaryRepository.findAllByDate(date);
    }

    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate){
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    public void updateDiary(LocalDate date, String text){
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.setText(text);
        //덮어쓰기
        diaryRepository.save(nowDiary);
    }

    //@Transactional 어노테이션 필요
    public void deleteDiary(LocalDate date){
        diaryRepository.deleteAllByDate(date);
    }

    private String getWeatherString(){
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;
        try {
            URL url = new URL(apiUrl);
            //요청을 보낼 수 있는 Http 커넥션을 열어주고 GET 요청을 보내고 응답 코드를 받음
            //응답 코드에 따라 오류 메시지 혹은 실제 응답 객체를 받아옴
            //이를 BufferedReader 안에 넣어둠
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            BufferedReader br;
            if(responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            //BufferedReader 안에 넣어둔 것을 하나하나 읽으면서 response라는 StringBuilder에 그 결괏값들을 쌓음
            //BufferdReader가 성능면에서 유리하기 때문에 사용
            //결과적으로 response 객체에 API를 호출하고 받은 결괏값들이 쌓임
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();
        } catch (Exception e) {
            return "failed to get response";
        }
    }

    private Map<String, Object> parseWeather(String jsonString) {
        //openweathermap에서 String값으로 받아온 데이터를 JSONParser를 이용하여 파싱하고, 그 안의 필요한 값을 해시맵 형태로 반환

        //googlecode에서 가져온 JSONParser 사용
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        //파싱 작업이 정상적으로 동작하지 않는 경우 핸들링을 위한 try-catch 문
        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //json 내 main, weather를 JSONObject로 가져온 후 그 안의 값도 가져와서 resultMap에 해쉬 형태로 넣기
        //main 내 온도값, weather 내 main, icon 데이터
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));
        //weather은 []대괄호 안에 있으므로 JSONArray로 받아서 Object로
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        //list안의 객체가 1개이므로 인덱스 = 0
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));
        return resultMap;


    }
}
