package ru.jimaltair.currencyconverter.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jimaltair.currencyconverter.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, String> {
    Currency findCurrencyByCharCode(String charCode);
}
