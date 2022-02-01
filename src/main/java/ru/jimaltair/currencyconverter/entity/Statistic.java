package ru.jimaltair.currencyconverter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Statistic {
    private String firstCurrency;
    private String secondCurrency;
    private double averageRate;
    private double overallSum;
    private LocalDate startDate;
    private LocalDate finishDate;
}
