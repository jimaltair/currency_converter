package ru.jimaltair.currencyconverter;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.entity.CurrencyRate;
import ru.jimaltair.currencyconverter.entity.XMLContent;
import ru.jimaltair.currencyconverter.repository.CurrencyRateRepository;
import ru.jimaltair.currencyconverter.repository.CurrencyRepository;
import ru.jimaltair.currencyconverter.service.XMLService;

import java.time.LocalDate;

@SpringBootApplication
public class CurrencyConverterApplication {

    private static final Currency ROUBLE = new Currency("643", "RUB", 1, "Российский рубль");
    private static final CurrencyRate ROUBLE_RATE = CurrencyRate.builder()
            .rate(1)
            .date(LocalDate.now())
            .currency(ROUBLE)
            .build();

    public static void main(String[] args) {
        SpringApplication.run(CurrencyConverterApplication.class, args);
    }

    @Bean
    ApplicationRunner initApplicationRunner(CurrencyRepository currencyRepository, CurrencyRateRepository currencyRateRepository){
        XMLContent xmlContent = XMLService.initXMLService();
        return args -> {
            currencyRepository.save(ROUBLE);
            currencyRepository.saveAll(xmlContent.getCurrencies());

            currencyRateRepository.save(ROUBLE_RATE);
            currencyRateRepository.saveAll(xmlContent.getCurrencyRates());
        };

    }

}
