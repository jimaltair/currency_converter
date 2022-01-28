package ru.jimaltair.currencyconverter.dto;

import lombok.Getter;

@Getter
public class ConvertForm {
    private String firstCurrency;
    private String secondCurrency;
    private double amount;
}
