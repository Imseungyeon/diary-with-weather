package syim.weather.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

@Service
public class DiaryService {

    //@Value를 통해 SpringBoot에 이미 지정되어 있는 변수 openweathermap.key의 값을 가져와서 apiKey에 넣어줄 것
    @Value("${openweathermap.key}")
    private String apiKey;

    public void createDiary(LocalDate date, String text){
        //open weather map에서 날씨 데이터 가져오기
        String weatherData = getWeatherString();
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
}
