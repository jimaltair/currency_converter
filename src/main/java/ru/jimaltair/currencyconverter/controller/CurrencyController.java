package ru.jimaltair.currencyconverter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.service.CurrencyConversionService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class CurrencyController {

    private final CurrencyConversionService conversionService;

    @GetMapping("/converter")
    public String getConverter(Model model){
        Iterable<Currency> currencies = conversionService.getAllCurrency();
        model.addAttribute("currencies", currencies);
        return "convertPage";
    }
}
