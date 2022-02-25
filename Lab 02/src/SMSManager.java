import java.time.LocalDateTime;
import java.util.ArrayList;

public interface SMSManager {
    public void insertSMS();
    public ArrayList<String> retrieveSMSByDate(LocalDateTime startDate, LocalDateTime endDate);
    public ArrayList<String> retrieveSMSByPromoCode(String promoCode);
    public ArrayList<String> retrieveSMSByMsisdn(String msisdn);
    public ArrayList<String> retrieveSMSSentBySystem();
    public ArrayList<String> retrieveSMSReceivedBySystem();
    public ArrayList<String> retrieveSMSBySeveralMsisdn(String... msisdn);
}
