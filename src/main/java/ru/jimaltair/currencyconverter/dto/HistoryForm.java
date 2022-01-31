package ru.jimaltair.currencyconverter.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class HistoryForm {
    private String firstCurrency;
    private String secondCurrency;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
