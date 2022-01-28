package ru.jimaltair.currencyconverter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.entity.CurrencyRate;
import ru.jimaltair.currencyconverter.entity.Exchange;
import ru.jimaltair.currencyconverter.entity.XMLContent;
import ru.jimaltair.currencyconverter.repository.CurrencyRateRepository;
import ru.jimaltair.currencyconverter.repository.CurrencyRepository;
import ru.jimaltair.currencyconverter.repository.ExchangeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyRateRepository currencyRateRepository;
    private final ExchangeRepository exchangeRepository;

    @Transactional
    public double calculateConversionResult(String firstCurrencyCode, String secondCurrencyCode, double amount) {
        /**
         * формула конвертации из валюты №1 в валюту №2 с использованием кросс-курса к рублю:
         *             R1 / Nom1
         * Res = N1 * -----------
         *             R2 / Nom2
         * где:
         *
         * Res - результат конвертации из валюты №1 в валюту №2
         * N1 - количество валюты №1, конвертацию который мы рассчитываем
         * R1 - отношение к рублю валюты №1
         * R2 - отношение к рублю валюты №2
         * Nom1 - номинал валюты №1
         * Nom2 - номинал валюты №2
         */
        log.info("Starting to calculate conversion from {} to {}", firstCurrencyCode, secondCurrencyCode);
        Currency cur1 = currencyRepository.findCurrencyByCharCode(firstCurrencyCode);
        Currency cur2 = currencyRepository.findCurrencyByCharCode(secondCurrencyCode);
        int cur1Nominal = cur1.getNominal();
        int cur2Nominal = cur2.getNominal();
        CurrencyRate curRate1 = currencyRateRepository.findByCurrency(cur1);
        CurrencyRate curRate2 = currencyRateRepository.findByCurrency(cur2);

        // если дата получения курса хотя бы одной из валют устарела, делаем запрос к ЦБР на получение актуальных курсов
        // и сохраняем эти данные в БД, после этого запрашиваем обновлённый курс вычисляемых валют
        LocalDate currentDate = LocalDate.now();
        if (curRate1.getDate().isBefore(currentDate) || curRate2.getDate().isBefore(currentDate)) {
            XMLContent xmlContent = XMLService.initXMLService();
            currencyRateRepository.saveAll(xmlContent.getCurrencyRates());
            curRate1 = currencyRateRepository.findByCurrency(cur1);
            curRate2 = currencyRateRepository.findByCurrency(cur2);
        }

        // вычисляем результат конвертации по формуле, приведённой в начале метода
        double result = amount * (curRate1.getRate() / cur1Nominal) / (curRate2.getRate() / cur2Nominal);
        log.info("The result of conversion is {} {}", result, secondCurrencyCode);
        Exchange exchange = Exchange.builder()
                .firstCurrency(cur1)
                .secondCurrency(cur2)
                .rate(curRate1.getRate())
                .amountFirstCurrency(amount)
                .resultOfConversion(result)
                .madeAt(LocalDateTime.now())
                .build();
        exchangeRepository.save(exchange);
        return result;
    }
}
