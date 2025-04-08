package school.faang.user_service.entity.premium;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum PremiumPeriod {

    ONE_MONTH(30, BigDecimal.valueOf(10)),
    THREE_MONTH(90, BigDecimal.valueOf(25)),
    ONE_YEAR(365, BigDecimal.valueOf(80));

    private int day;
    private BigDecimal price;

    PremiumPeriod(int day, BigDecimal price) {
        this.day = day;
        this.price = price;
    }

    public static PremiumPeriod fromDays(int days) {
        for (PremiumPeriod period : PremiumPeriod.values()) {
            if (period.day == days) {
                return period;
            }
        }
        throw new IllegalArgumentException("Invalid number of days: " + days);
    }

}
