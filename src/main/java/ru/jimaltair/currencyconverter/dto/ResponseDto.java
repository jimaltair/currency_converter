package ru.jimaltair.currencyconverter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
    private double result;
    private String message;
}
