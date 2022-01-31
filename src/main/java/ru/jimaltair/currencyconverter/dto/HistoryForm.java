package ru.jimaltair.currencyconverter.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HistoryForm {
    private String firstCurrency;
    private String secondCurrency;
    private LocalDate date;
}
