package syim.weather.error;

public class InvalidDate extends RuntimeException{
    private static final String MESSAGE = "지원하지 않는 날짜입니다.";

    public InvalidDate(){
        super(MESSAGE);
    }
}
