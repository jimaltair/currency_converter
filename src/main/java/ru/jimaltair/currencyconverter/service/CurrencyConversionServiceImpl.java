package ru.jimaltair.currencyconverter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.jimaltair.currencyconverter.entity.*;
import ru.jimaltair.currencyconverter.repository.CurrencyRateRepository;
import ru.jimaltair.currencyconverter.repository.CurrencyRepository;
import ru.jimaltair.currencyconverter.repository.ExchangeRepository;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Сервисный класс, содержащий основную логику приложения: конвертирование валют, получение списка валют и
 * истории их конвертаций из репозитория, вычисление статистической информации.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyRateRepository currencyRateRepository;
    private final ExchangeRepository exchangeRepository;

    /**
     * Метод, производящий конвертацию валютной пары с использованием кросс-курса к рублю.
     *
     * @param firstCurrencyCode  - валюта, которую нужно конвертировать
     * @param secondCurrencyCode - валюта, в которую конвертируем
     * @param amount             - количество конвертируемой валюты
     * @return результат конвертации - число типа {@code double}
     */
    @Override
    public double calculateConversionResult(String firstCurrencyCode, String secondCurrencyCode, double amount) {
        /**
         * формула конвертации из валюты №1 в валюту №2 с использованием кросс-курса к рублю:
         *             R1 / Nom1
         * Res = N1 * -----------
         *             R2 / Nom2
         * где:
         *
         * Res - результат конвертации из валюты №1 в валюту №2
         * N1 - количество валюты №1, конвертацию которой мы рассчитываем
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
        CurrencyRate curRate1 = currencyRateRepository.findTopByCurrency(cur1);
        CurrencyRate curRate2 = currencyRateRepository.findTopByCurrency(cur2);

        // если дата получения курса хотя бы одной из валют устарела (кроме воскресенья), делаем запрос к ЦБР на получение
        // актуальных курсов и сохраняем эти данные в БД, после этого запрашиваем обновлённый курс вычисляемых валют
        LocalDate currentDate = LocalDate.now();
        if (currentDate.getDayOfWeek() != DayOfWeek.SUNDAY && (curRate1.getDate().isBefore(currentDate)
                || curRate2.getDate().isBefore(currentDate))) {
            log.info("Try to load new rates from CBR for {}", currentDate);
            XMLContent xmlContent = XMLService.initXMLService();
            currencyRateRepository.saveAll(xmlContent.getCurrencyRates());
            curRate1 = currencyRateRepository.findTopByCurrency(cur1);
            curRate2 = currencyRateRepository.findTopByCurrency(cur2);
        }

        // вычисляем результат конвертации по формуле, приведённой в начале метода
        double crossRate = (curRate1.getRate() / cur1Nominal) / (curRate2.getRate() / cur2Nominal);
        double result = amount * crossRate;
        // округляем результат до 4-ёх знаков после запятой
        result = roundToFourDecimalPlates(result);
        crossRate = roundToFourDecimalPlates(crossRate);
        log.info("The result of conversion is {} {}", result, secondCurrencyCode);
        // сохраняем произведенный обмен в соответствующий репозиторий
        Exchange exchange = Exchange.builder()
                .firstCurrency(cur1)
                .secondCurrency(cur2)
                .rate(crossRate)
                .amountFirstCurrency(amount)
                .resultOfConversion(result)
                .madeAt(LocalDate.now())
                .build();
        exchangeRepository.save(exchange);
        return result;
    }

    @Override
    public Iterable<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    /**
     * Метод, позволяющий получить историю обменов произведённых по валютной паре за определённую дату.
     *
     * @param firstCurrency  - валюта, которую нужно конвертировать
     * @param secondCurrency - валюта, в которую конвертируем
     * @param date           - дата, за которую ищем историю произведённых обменов
     * @return {@link Iterable}, параметризованный объектом {@link Currency} - содержит в себе всю историю обменов
     * валютной пары за указанную дату
     */
    @Override
    public List<Exchange> getHistory(String firstCurrency, String secondCurrency, LocalDate date) {
        return exchangeRepository.findAllByFirstCurrencyCharCodeAndSecondCurrencyCharCodeAndMadeAt(firstCurrency, secondCurrency, date);
    }

    /**
     * Метод, позволяющий получить статистическую информацию по обмену валютной пары в указанном диапазоне дат.
     *
     * @param firstCurrency  - валюта, которую нужно конвертировать
     * @param secondCurrency - валюта, в которую конвертируем
     * @param startDate      - дата начала периода, за который ведётся подсчёт статистики
     * @param finishDate     - конечная дата периода, за который ведётся подсчёт статистики
     * @return объект {@link Statistic}, поля которого содержат в себе входящие параметры, а также средний курс конвертации и
     * общую сумму сконвертированных средств
     */
    @Override
    public Statistic getStatistic(String firstCurrency, String secondCurrency, LocalDate startDate, LocalDate finishDate) {
        // получаем список дат в указанном диапазоне с шагом в 1 день
        List<LocalDate> dates = Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, finishDate.plusDays(1)))
                .collect(Collectors.toList());
        // получаем историю обменов по каждому дню из полученного диапазона дат
        List<Exchange> weekHistory = new ArrayList<>();
        for (LocalDate date : dates) {
            weekHistory.addAll(getHistory(firstCurrency, secondCurrency, date));
        }
        // если за указанную неделю обменов не было, вернём пустой объект статистики
        if (weekHistory.isEmpty()) {
            log.warn("There is no statistic for {} and {} for last week", firstCurrency, secondCurrency);
            return new Statistic();
        }
        // вычисляем средний курс конвертаций по указанной паре за неделю
        double averageRate = weekHistory.stream()
                .mapToDouble(Exchange::getRate)
                .average()
                .getAsDouble();
        averageRate = roundToFourDecimalPlates(averageRate);
        // вычисляем общую сумму конвертаций по указанной паре за неделю
        double overallSum = weekHistory.stream()
                .mapToDouble(Exchange::getResultOfConversion)
                .sum();
        overallSum = roundToFourDecimalPlates(overallSum);
        return new Statistic(firstCurrency, secondCurrency, averageRate, overallSum, startDate, finishDate);
    }

    private double roundToFourDecimalPlates(double number) {
        return Double.parseDouble(new DecimalFormat("#.####").format(number).replace(',', '.'));
    }
}
