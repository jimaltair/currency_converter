package ru.jimaltair.currencyconverter.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.jimaltair.currencyconverter.dto.ConvertForm;
import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.service.CurrencyConversionService;
import ru.jimaltair.currencyconverter.service.CurrencyConversionServiceImpl;

@Slf4j
@Controller
@AllArgsConstructor
public class CurrencyController {

    private final CurrencyConversionService conversionService;

    @GetMapping("/converter")
    public ModelAndView getConverter() {
        Iterable<Currency> currencies = conversionService.getAllCurrencies();
        ModelAndView mv = new ModelAndView("converter");
        mv.addObject("currencies", currencies);
        mv.addObject("convertForm", new ConvertForm());
        return mv;
    }

//    @PostMapping("/converter")
//    public String conversionSubmit(@RequestBody ConvertForm convertForm) {
//        log.info("Starting to calculate conversion");
//        double result = conversionService.calculateConversionResult(convertForm.getFirstCurrency(),
//                convertForm.getSecondCurrency(), convertForm.getAmount());
//        log.info("The result of conversion is {} {}", result, convertForm.getSecondCurrency());
//        return "convertPage";
//    }

    @PostMapping("/converter")
//    @ResponseBody
    public String convert(@ModelAttribute ConvertForm convertForm) {
        log.info("Starting to calculate conversion");
//        ResponseDto responseDto = new ResponseDto();
        double result = conversionService.calculateConversionResult(convertForm.getFirstCurrency(), convertForm.getSecondCurrency(),
                convertForm.getAmount());
        log.info("The result of conversion is {} {}", result, convertForm.getSecondCurrency());
//        responseDto.setResult(result);
        return "test";
    }
}
