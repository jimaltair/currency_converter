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
import ru.jimaltair.currencyconverter.entity.Statistic;
import ru.jimaltair.currencyconverter.service.CurrencyConversionService;

import javax.validation.Valid;
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
    public ModelAndView convert(@ModelAttribute @Valid ConvertForm convertForm, BindingResult bindingResult) {
        log.info("Starting to calculate conversion");
        ModelAndView mv = new ModelAndView("converter");
        Iterable<Currency> currencies = conversionService.getAllCurrencies();
        mv.addObject("currencies", currencies);
        if (bindingResult.hasErrors()) {
            log.error("The field 'amount' is negative");
            return mv;
        }
        double result = conversionService.calculateConversionResult(convertForm.getFirstCurrency(),
                convertForm.getSecondCurrency(), convertForm.getAmount());
        mv.addObject("result", result);
        log.info("The conversion is completed");
        return mv;
    }

    @GetMapping("/history")
    public ModelAndView getHistoryPage(@ModelAttribute HistoryForm historyForm) {
        ModelAndView mv = new ModelAndView("history");
        Iterable<Currency> currencies = conversionService.getAllCurrencies();
        mv.addObject("currencies", currencies);
        mv.addObject("weekStatistic", new Statistic());
        return mv;
    }

    @PostMapping("/history")
    public ModelAndView getHistory(@ModelAttribute @Valid HistoryForm historyForm, BindingResult bindingResult) {
        log.info("Starting to search conversion history");
        ModelAndView mv = new ModelAndView("history");
        Iterable<Currency> currencies = conversionService.getAllCurrencies();
        mv.addObject("currencies", currencies);
        if (bindingResult.hasErrors()) {
            // кладём пустой объект статистики чтобы не словить 500-ую ошибку
            mv.addObject("weekStatistic", Statistic.getNullStatistic());
            log.error("The field 'date' is in future");
            return mv;
        }
        // получаем историю конвертаций валютной пары за указанную дату
        List<Exchange> exchangeHistory = conversionService.getHistory(historyForm.getFirstCurrency(),
                historyForm.getSecondCurrency(), historyForm.getDate());
        log.info("Received the history with {} records", exchangeHistory.size());
        // получаем статистику валютной пары за неделю
        Statistic weekStatistic = conversionService.getStatistic(historyForm.getFirstCurrency(),
                historyForm.getSecondCurrency(), historyForm.getDate().minusDays(6), historyForm.getDate());
        log.info("Received the week statistic with averageRate={} and overallSum={}", weekStatistic.getAverageRate(),
                weekStatistic.getOverallSum());
        mv.addObject("exchangeHistory", exchangeHistory);
        mv.addObject("weekStatistic", weekStatistic);

        return mv;
    }
}
