import database.Database;

import java.time.LocalDateTime;

public class Promo {
    protected String promoCode;
    protected String details;
    protected String shortCode;
    protected LocalDateTime startDate;
    protected LocalDateTime endDate;

    // Promo Constructor
    public Promo(String promoCode,
                 String details,
                 String shortCode,
                 LocalDateTime startDate,
                 LocalDateTime endDate) {
        this.promoCode = promoCode;
        this.details = details;
        this.shortCode = shortCode;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Promo Getters and Setters
    public String getPromoCode() {
        return promoCode;
    }

    public String getDetails() {
        return details;
    }

    public String getShortCode() {
        return shortCode;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    // Promo Data Population Insert Function
    public void insertPromo() {
        String insertQuery = "INSERT INTO promo (promo_code, details, short_code, start_date, end_date) " +
                "VALUES ('" +
                getPromoCode() + "', '" +
                getDetails() + "', '" +
                getShortCode() + "', '" +
                getStartDate() + "', '" +
                getEndDate() + "');";

        Database.insertData(insertQuery);
    }

}
