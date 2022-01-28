package ru.jimaltair.currencyconverter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.service.CurrencyConversionService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class CurrencyController {

    private final CurrencyConversionService conversionService;

    @GetMapping("/converter")
    public ModelAndView getConverter(){
        Iterable<Currency> currencies = conversionService.getAllCurrency();
        return new ModelAndView("convertPage", "currencies", currencies);
    }
}
