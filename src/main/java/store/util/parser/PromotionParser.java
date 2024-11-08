package store.util.parser;

import store.domain.Promotion;

import java.time.LocalDate;

public class PromotionParser {

    public static Promotion parse(String line) {
        String[] data = line.split(",");
        return new Promotion(
                data[0],
                Integer.parseInt(data[1]),
                Integer.parseInt(data[2]),
                LocalDate.parse(data[3]),
                LocalDate.parse(data[4])
        );
    }
}
