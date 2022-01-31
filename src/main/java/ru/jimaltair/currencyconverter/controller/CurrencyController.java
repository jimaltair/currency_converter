package ru.jimaltair.currencyconverter.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.jimaltair.currencyconverter.dto.ConvertForm;
import ru.jimaltair.currencyconverter.dto.HistoryForm;
import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.entity.Exchange;
import ru.jimaltair.currencyconverter.service.CurrencyConversionService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
public class CurrencyController {

    private final CurrencyConversionService conversionService;

    @GetMapping("/converter")
    public ModelAndView getConvertPage(@ModelAttribute @Valid ConvertForm convertForm) {
        Iterable<Currency> currencies = conversionService.getAllCurrencies();
        ModelAndView mv = new ModelAndView("converter");
        mv.addObject("currencies", currencies);
        return mv;
    }

    @PostMapping("/converter")
    public ModelAndView convert(@ModelAttribute @Valid ConvertForm convertForm) {
        log.info("Starting to calculate conversion");

        Iterable<Currency> currencies = conversionService.getAllCurrencies();
        double result = conversionService.calculateConversionResult(convertForm.getFirstCurrency(), convertForm.getSecondCurrency(),
                convertForm.getAmount());
        ModelAndView mv = new ModelAndView("converter");
        mv.addObject("currencies", currencies);
        mv.addObject("result", result);

        log.info("The conversion is completed");
        return mv;
    }

    @GetMapping("/history")
    public ModelAndView getHistoryPage(@ModelAttribute HistoryForm historyForm) {
        ModelAndView mv = new ModelAndView("history");
        Iterable<Currency> currencies = conversionService.getAllCurrencies();
        mv.addObject("currencies", currencies);
        return mv;
    }

    @PostMapping("/history")
    public ModelAndView getHistory(@ModelAttribute HistoryForm historyForm) {
        log.info("Starting to search conversion history");
        ModelAndView mv = new ModelAndView("history");
        Iterable<Currency> currencies = conversionService.getAllCurrencies();
        mv.addObject("currencies", currencies);

        List<Exchange> exchangeHistory = conversionService.getHistory(historyForm.getFirstCurrency(),
                historyForm.getSecondCurrency(), historyForm.getDate());
        log.info("Received the history with {} records", exchangeHistory.size());
        mv.addObject("exchangeHistory", exchangeHistory);

        return mv;
    }
}
