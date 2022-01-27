package ru.jimaltair.currencyconverter.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.entity.CurrencyRate;

public interface CurrencyRateRepository extends CrudRepository<CurrencyRate, Long> {
    CurrencyRate findByCurrency(Currency currency);
}
