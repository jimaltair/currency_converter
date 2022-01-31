package ru.jimaltair.currencyconverter.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.entity.Exchange;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRepository extends CrudRepository<Exchange, Long> {
    List<Exchange> findAllByFirstCurrencyCharCodeAndSecondCurrencyCharCodeAndMadeAt(String firstCurrency,
                                                                                    String secondCurrency,
                                                                                    LocalDate date);
}
