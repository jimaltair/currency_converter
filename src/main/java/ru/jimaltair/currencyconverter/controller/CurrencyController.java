package ru.jimaltair.currencyconverter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.jimaltair.currencyconverter.dto.ConvertForm;
import ru.jimaltair.currencyconverter.dto.ResponseDto;
import ru.jimaltair.currencyconverter.entity.Currency;
import ru.jimaltair.currencyconverter.service.CurrencyConversionService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class CurrencyController {

    private final CurrencyConversionService conversionService;

    @GetMapping("/converter")
    public ModelAndView getConverter() {
        log.info("ЗАПУСКАЮ КОНВЕРТЕР");
        Iterable<Currency> currencies = conversionService.getAllCurrency();
        return new ModelAndView("convertPage", "currencies", currencies);
    }

    @PostMapping("/converter")
    public ResponseEntity<ResponseDto> convertCurrencies(@RequestBody ConvertForm convertForm){
        ResponseDto responseDto = new ResponseDto();
        double result = conversionService.calculateConversionResult(convertForm.getFirstCurrency(),
                convertForm.getSecondCurrency(), convertForm.getAmount());
        responseDto.setResult(result);
        return ResponseEntity.ok(responseDto);
    }
}
