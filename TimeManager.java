import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

public class TimeManager {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static boolean isValid(LocalDate startDate, LocalDate endDate) throws TimeoutException{
        LocalDate now=LocalDate.now();
        boolean isInRange=(now.isEqual(startDate)|| now.isAfter(startDate)) && ((now.isEqual(endDate)) || now.isBefore(endDate));
        if (!isInRange){
            if(now.isBefore(startDate)){
                String message=String.format("Project hasn't started yet. Current time: %s, Project starts at: %s",now.format(FORMATTER), startDate.format(FORMATTER));
                throw new TimeoutException(message);
            }
            else if(now.isAfter(endDate)){
                String message=String.format("Project closed. Current time: %s, Project ended at: %s",now.format(FORMATTER), endDate.format(FORMATTER));
                throw new TimeoutException(message);
            } 
        }
        return true;
    }

}
