package ru.jimaltair.currencyconverter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvertForm {
    private String firstCurrency;
    private String secondCurrency;
    private double amount;
}
