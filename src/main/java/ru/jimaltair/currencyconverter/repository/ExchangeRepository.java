package ru.jimaltair.currencyconverter.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jimaltair.currencyconverter.entity.Exchange;

public interface ExchangeRepository extends CrudRepository<Exchange, Long> {
}
