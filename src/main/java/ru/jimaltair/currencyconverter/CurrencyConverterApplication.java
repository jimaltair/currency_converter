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

    public static void main(String[] args) {
        SpringApplication.run(CurrencyConverterApplication.class, args);
    }

    @Bean
    ApplicationRunner initApplicationRunner(CurrencyRepository currencyRepository, CurrencyRateRepository currencyRateRepository) {
        // после запуска приложения считываем актуальные курсы с сайта ЦБ РФ
        XMLContent xmlContent = XMLService.initXMLService();
        return args -> {
            currencyRepository.saveAll(xmlContent.getCurrencies());
            currencyRateRepository.saveAll(xmlContent.getCurrencyRates());
        };

    }

}
