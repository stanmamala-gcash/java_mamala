import database.Database;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    final private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        // Connect to SMS Database
        Database.connect();

        // Database Population Codes
        // -----PROMO POPULATION-----
/*
        Promo promo1 = new Promo("PROMO",
                 "Get one party size pizza for 1 PHP only! " +
                        "Promo runs from Feb 1, 10am until June 30. " +
                        "Just text REGISTER to 123455. Then reply <Lastname, Firstname>.",
                "123455",
                        LocalDateTime.parse("2022-02-01 10:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        LocalDateTime.parse("2022-06-30 23:59:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        promo1.insertPromo();

        Promo promo2 = new Promo("WINGS",
                "Get 6 pcs. of our famous wings for 1 PHP only! " +
                        "Promo runs from Feb 1, 10am until July 21. " +
                        "Just text REGISTER to 14300. Then reply <Lastname, Firstname>.",
                "14300",
                LocalDateTime.parse("2022-02-01 10:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse("2022-07-21 23:59:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        promo2.insertPromo();

        Promo promo3 = new Promo("MLEPIC",
                "Get one Mobile Legends Epic Skin for 1 PHP only! " +
                        "Promo runs from Feb 1, 10am until Aug. 10. " +
                        "Just text REGISTER to 543211. Then reply <Lastname, Firstname>.",
                "543211",
                LocalDateTime.parse("2022-02-01 10:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse("2022-08-10 23:59:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        promo3.insertPromo();
*/

        // -----SMS POPULATION-----
        // Program
        Scanner input = new Scanner(System.in);
        boolean polling = true;
        String sender = "";
        String recipient = "";
        Map<String, String> sms = new HashMap<>();
        Integer step = 1;

        while (polling) {
            logger.log(Level.INFO, "Start");

            if (step == 1) {
                sender = "";
                recipient = "";
            }

            while (sender.isEmpty()) {
                System.out.println("Enter your mobile number: ");
                sender = input.nextLine();
                sms.put("sender", sender);
            }

            System.out.println("Enter message: ");
            String message = input.nextLine();
            sms.put("message", message);

            while (recipient.isEmpty()) {
                System.out.println("Send to: ");
                recipient = input.nextLine();
                sms.put("recipient", recipient);
            }

            step = SMSChecker.smsChecker(sms, step);
        }

        // -----DATA RETRIEVAL-----
//        SMS retrieve = new SMS();

//        ArrayList<String> smsByDate = retrieve.retrieveSMSByDate(
//                LocalDateTime.parse("2022-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
//                LocalDateTime.parse("2022-12-31 23:59:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        ArrayList<String> smsByPromoCode = retrieve.retrieveSMSByPromoCode("PROMO");
//        ArrayList<String> smsByMsisdn = retrieve.retrieveSMSByMsisdn("+639064611492");
//        ArrayList<String> smsSentBySystem = retrieve.retrieveSMSSentBySystem();
//        ArrayList<String> smsReceivedBySystem = retrieve.retrieveSMSReceivedBySystem();
//        ArrayList<String> smsBySeveralMsisdn = retrieve.retrieveSMSBySeveralMsisdn("+639064611492", "+639269267826");

//        for (int i = 0; i < smsReceivedBySystem.size(); i++) {
//            System.out.print(smsReceivedBySystem.get(i));
//        }

        // -----REPORTS-----
//        Database.retrieveSMSBySMSStatus("FAILED");
//        Database.retrieveSMSPerTypeBySMSStatus("FAILED");
//        Database.retrieveListOfPeople();
//       SMS.retrieveSMSCountReceivedBySystem();
//        SMS.retrieveSMSCountSentBySystem();

        // Disconnect to SMS Database
        Database.disconnect();
    }
}
