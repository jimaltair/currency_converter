package ru.jimaltair.currencyconverter.service;

import ru.jimaltair.currencyconverter.entity.Currency;

public interface CurrencyConversionService {

    double calculateConversionResult(String firstCurrencyCode, String secondCurrencyCode, double amount);

    Iterable<Currency> getAllCurrencies();
}
