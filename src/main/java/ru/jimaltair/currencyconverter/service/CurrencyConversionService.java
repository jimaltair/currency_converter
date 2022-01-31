package ru.jimaltair.currencyconverter.service;

import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.entity.Exchange;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyConversionService {

    double calculateConversionResult(String firstCurrencyCode, String secondCurrencyCode, double amount);

    Iterable<Currency> getAllCurrencies();

    List<Exchange> getHistory(String firstCurrency, String secondCurrency, LocalDate date);
}
