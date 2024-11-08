package store.domain;

import java.time.LocalDate;

import camp.nextstep.edu.missionutils.DateTimes;

public class Promotion {
    private String name;
    private int buy;
    private int get;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public boolean promotionNameEqual(String promotionName) {
        return (this.name.equals(promotionName));
    }

    public boolean isValidOnDate() {
        LocalDate today = DateTimes.now().toLocalDate();
        return (today.equals(startDate) || today.isAfter(startDate)) && (today.equals(endDate) || today.isBefore(endDate));
    }
}
