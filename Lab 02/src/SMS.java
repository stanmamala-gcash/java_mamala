import database.Database;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMS implements SMSManager{
    final private static Logger logger = Logger.getLogger(SMS.class.getName());

    protected String RECIPIENT;
    protected String SENDER;
    protected String TRANSACTION_ID;
    protected String MESSAGE;
    protected String STATUS;
    protected LocalDateTime TIMESTAMP;

    // SMS Constructor
    public SMS() {

    }

    public SMS(String SENDER,
               String MESSAGE,
               String RECIPIENT,
               String TRANSACTION_ID,
               LocalDateTime TIMESTAMP) {
        this.RECIPIENT = RECIPIENT;
        this.SENDER = SENDER;
        this.TRANSACTION_ID = TRANSACTION_ID;
        this.MESSAGE = MESSAGE;
        this.TIMESTAMP = TIMESTAMP;
    }

    // SMS Getters and Setters
    public String getRECIPIENT() {
        return RECIPIENT;
    }

    public String getSENDER() {
        return SENDER;
    }

    public String getTRANSACTION_ID() {
        return TRANSACTION_ID;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public LocalDateTime getTIMESTAMP() {
        return TIMESTAMP;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }


    // Functions from SMS Manager Interface
    // 1. Insert SMS
    @Override
    public void insertSMS() {
        String insertQuery = "INSERT INTO sms (sender, message, recipient, transaction_id, timestamp, status) " +
                "VALUES ('" +
                getSENDER() + "', '" +
                getMESSAGE() + "', '" +
                getRECIPIENT() + "', '" +
                getTRANSACTION_ID() + "', '" +
                getTIMESTAMP() + "', '" +
                getSTATUS() + "');";

        Database.insertData(insertQuery);
    }

    // 2. Retrieve SMS Given a Start Date and End Date
    @Override
    public ArrayList<String> retrieveSMSByDate(LocalDateTime startDate, LocalDateTime endDate) {
        String selectQuery = "SELECT * FROM sms " +
                             "WHERE timestamp BETWEEN '" + startDate + "' AND '" + endDate + "' " +
                             "ORDER BY timestamp;";

        return Database.retrieveSMSData(selectQuery);
    }

    // 3. Retrieve SMS Given a Promo Code
    @Override
    public ArrayList<String> retrieveSMSByPromoCode(String promoCode) {
        String selectQueryPromo = "SELECT short_code FROM promo WHERE promo_code='" + promoCode + "';";
        ArrayList<String> shortCode = Database.retrievePromoList(selectQueryPromo);
        String selectQuerySMS = "SELECT * FROM sms " +
                             "WHERE recipient='" + shortCode.get(0) + "' OR sender='" + shortCode.get(0) + "' " +
                             "ORDER BY timestamp;";

        return Database.retrieveSMSData(selectQuerySMS);
    }

    // 4. Retrieve SMS Given an MSISDN
    @Override
    public ArrayList<String> retrieveSMSByMsisdn(String msisdn) {
        String selectQuery = "SELECT * FROM sms " +
                             "WHERE sender ='" + msisdn + "' " +
                             "ORDER BY timestamp;";

        return Database.retrieveSMSData(selectQuery);
    }


    // 5. Retrieve SMS Sent By the System
    @Override
    public ArrayList<String> retrieveSMSSentBySystem() {
        String selectQueryPromo = "SELECT short_code FROM promo;";
        ArrayList<String> shortCode = Database.retrievePromoList(selectQueryPromo);
        StringBuilder selectQuerySMS = new StringBuilder("SELECT * FROM sms " +
                "WHERE sender IN (");
        for (String str : shortCode) {
            selectQuerySMS.append("'").append(str).append("', ");
        }
        selectQuerySMS = new StringBuilder(selectQuerySMS.substring(0, selectQuerySMS.length() - 2) + ") ORDER BY timestamp;");

        return Database.retrieveSMSData(selectQuerySMS.toString());
    }

    // 6. Retrieve SMS Received By the System
    @Override
    public ArrayList<String> retrieveSMSReceivedBySystem() {
        String selectQueryPromo = "SELECT short_code FROM promo;";
        ArrayList<String> shortCode = Database.retrievePromoList(selectQueryPromo);
        StringBuilder selectQuerySMS = new StringBuilder("SELECT * FROM sms " +
                "WHERE recipient IN (");
        for (String str : shortCode) {
            selectQuerySMS.append("'").append(str).append("', ");
        }
        selectQuerySMS = new StringBuilder(selectQuerySMS.substring(0, selectQuerySMS.length() - 2) + ") ORDER BY timestamp;");

        return Database.retrieveSMSData(selectQuerySMS.toString());
    }

    // 7. Retrieve SMS Given Several MSISDN
    @Override
    public ArrayList<String> retrieveSMSBySeveralMsisdn(String... msisdn) {
        StringBuilder selectQuery = new StringBuilder("SELECT * FROM sms " +
                "WHERE sender IN (");
        for (String str : msisdn) {
            selectQuery.append("'").append(str).append("',");
        }
        selectQuery = new StringBuilder(selectQuery.substring(0, selectQuery.length() - 2) + "') ORDER BY timestamp;");

        return Database.retrieveSMSData(selectQuery.toString());
    }

    // Some Function for Data Generation
    // 1. Total Count of SMS Received By the System
    public static void retrieveSMSCountReceivedBySystem() {
        SMS report = new SMS();
        ArrayList<String> list = report.retrieveSMSReceivedBySystem();
        Integer count = 0;
        for(String str : list) {
            count++;
        }
        logger.log(Level.INFO, "Total Count of SMS Received By The System: {0}", count);
    }

    // 2. Total Count of SMS Sent By the System
    public static void retrieveSMSCountSentBySystem() {
        SMS report = new SMS();
        ArrayList<String> list = report.retrieveSMSSentBySystem();
        Integer count = 0;
        for(String str : list) {
            count++;
        }
        logger.log(Level.INFO, "Total Count of SMS Sent By The System: {0}", count);
    }
}
