package ru.jimaltair.currencyconverter.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class ConvertForm {
    private String firstCurrency;
    private String secondCurrency;
    @PositiveOrZero
    private double amount;
}
