import database.Database;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMSChecker {
    final private static Logger logger = Logger.getLogger(Main.class.getName());


    public static int smsChecker(Map<String, String> smsToCheck, Integer step) {
        LocalDateTime timeSent = LocalDateTime.now();
        String transID = generateTransID();

        SMS newSMS = new SMS(smsToCheck.get("sender"), smsToCheck.get("message"), smsToCheck.get("recipient"), transID, timeSent);

        String selectQueryPromo = "SELECT promo_code FROM promo WHERE short_code='" + smsToCheck.get("recipient") + "';";
        ArrayList<String> promoList = Database.retrievePromoList(selectQueryPromo);

        if (promoList.size() == 1) {
            String selectQueryPromoStartDate = "SELECT start_date FROM promo WHERE short_code='" + smsToCheck.get("recipient") + "';";
            String selectQueryPromoEndDate = "SELECT end_date FROM promo WHERE short_code='" + smsToCheck.get("recipient") + "';";

            ArrayList<String> startDateList = Database.retrievePromoList(selectQueryPromoStartDate);
            ArrayList<String> endDateList = Database.retrievePromoList(selectQueryPromoEndDate);

            LocalDateTime startDate = LocalDateTime.parse(startDateList.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime endDate = LocalDateTime.parse(endDateList.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            if (step == 1 && Objects.equals(smsToCheck.get("message"), promoList.get(0))) {
                if (timeSent.isAfter(startDate) && timeSent.isBefore(endDate)) {
                   if (Objects.equals(smsToCheck.get("message"), "PROMO") || Objects.equals(smsToCheck.get("message"), "MLEPIC") || Objects.equals(smsToCheck.get("message"), "WINGS")) {
                        newSMS.setSTATUS("SUCCESSFUL");
                        newSMS.insertSMS();
                        systemSMS(smsToCheck.get("recipient"), smsToCheck.get("sender"), 5);
                        step = 2;
                    } else {
                        newSMS.setSTATUS("FAILED");
                        newSMS.insertSMS();
                        systemSMS(smsToCheck.get("recipient"), smsToCheck.get("sender"), 3);
                    }
                } else {
                    newSMS.setSTATUS("FAILED");
                    newSMS.insertSMS();
                    systemSMS(smsToCheck.get("recipient"), smsToCheck.get("sender"), 2);
                }
            } else if (step == 2) {
                if (Objects.equals(smsToCheck.get("message"), "REGISTER")) {
                    newSMS.setSTATUS("SUCCESSFUL");
                    newSMS.insertSMS();
                    systemSMS(smsToCheck.get("recipient"), smsToCheck.get("sender"), 6);
                    step = 3;
                } else {
                    newSMS.setSTATUS("FAILED");
                    newSMS.insertSMS();
                    systemSMS(smsToCheck.get("recipient"), smsToCheck.get("sender"), 3);
                }
            } else if (step == 3) {
                if (smsToCheck.get("message").contains(",")) {
                    newSMS.setSTATUS("SUCCESSFUL");
                    newSMS.insertSMS();
                    systemSMS(smsToCheck.get("recipient"), smsToCheck.get("sender"), 7);
                    step = 1;
                } else {
                    newSMS.setSTATUS("FAILED");
                    newSMS.insertSMS();
                    systemSMS(smsToCheck.get("recipient"), smsToCheck.get("sender"), 4);
                }
            }
        } else {
            newSMS.setSTATUS("FAILED");
            newSMS.insertSMS();
            systemSMS(smsToCheck.get("recipient"), smsToCheck.get("sender"), 1);
        }


        return step;
    }

    protected static String generateTransID() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder str = new StringBuilder();
        Random rnd = new Random();
        while (str.length() < 10) {
            int index = (int) (rnd.nextFloat() * chars.length());
            str.append(chars.charAt(index));
        }
        return str.toString();
    }

    protected static void systemSMS(String sender, String recipient, Integer code) {
        LocalDateTime timeSent = LocalDateTime.now();
        String transID = generateTransID();

        HashMap<Integer, String> static_SMS = new HashMap<>();
        static_SMS.put(1, "[SMS FAILED] This promo does not exist. Please try again.");
        static_SMS.put(2, "[SMS FAILED] The has expired. It is no longer available. Please try other promos.");
        static_SMS.put(3, "[SMS FAILED] The promo code you entered is invalid. Please try again.");
        static_SMS.put(4, "[SMS FAILED] Please check you name format and/or your recipient.");
        static_SMS.put(5, "[SMS SUCCESSFUL] To continue with the promo registration, just send REGISTER to " + sender);
        static_SMS.put(6, "[SMS SUCCESSFUL] To complete the promo registration, please send your Lastname, Firstname to " + sender);
        static_SMS.put(7, "[SMS SUCCESSFUL] Congratulations! You are now registered to this promo. ");

        SMS sysSMS = new SMS(sender, static_SMS.get(code), recipient, transID, timeSent);
        sysSMS.setSTATUS("SUCCESSFUL");
        sysSMS.insertSMS();
        logger.log(Level.INFO, static_SMS.get(code) + "\n");
    }
}
