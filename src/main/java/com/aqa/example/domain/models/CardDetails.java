package com.aqa.example.domain.models;

import com.aqa.example.core.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class CardDetails {
    private String number;
    private String expDate;
    private String cvv;
    private String cardholder;

    public YearMonth getExpYearMonth() {
        return YearMonth.parse(expDate,  DateTimeFormatter.ofPattern(Constants.PAYMENT_CARD_EXP_DATE_FORMAT));
    }
}
