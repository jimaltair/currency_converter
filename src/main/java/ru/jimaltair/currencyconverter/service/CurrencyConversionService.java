package ru.jimaltair.currencyconverter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.entity.CurrencyRate;
import ru.jimaltair.currencyconverter.repository.CurrencyRateRepository;
import ru.jimaltair.currencyconverter.repository.CurrencyRepository;
import ru.jimaltair.currencyconverter.repository.ExchangeRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyRateRepository currencyRateRepository;
    private final ExchangeRepository exchangeRepository;

    public double calculateConversionResult(String firstCurrencyCode, String secondCurrencyCode, double amount) {
        Currency cur1 = currencyRepository.findCurrencyByCharCode(firstCurrencyCode);
        Currency cur2 = currencyRepository.findCurrencyByCharCode(secondCurrencyCode);
        int cur1Nominal = cur1.getNominal();
        int cur2Nominal = cur2.getNominal();
        CurrencyRate curRate1 = currencyRateRepository.findByCurrency(cur1);
        CurrencyRate curRate2 = currencyRateRepository.findByCurrency(cur2);

        /**
         * формула конвертации из валюты №1 в валюту №2:
         * результат = количество вал1 * (отношение к рублю вал1 / номинал вал1) / (отношение к рублю вал2 / номинал вал2)
         */
        double result = amount * (curRate1.getRate() / cur1Nominal) / (curRate2.getRate() / cur2Nominal);


        return result;
    }
}
