package ru.jimaltair.currencyconverter.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_currency_code")
    private Currency firstCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_currency_code")
    private Currency secondCurrency;

    @Column(nullable = false)
    private double rate;

    @Column(nullable = false)
    private double amountFirstCurrency;

    @Column(nullable = false)
    private double resultOfConversion;

    @Column(nullable = false)
    private LocalDate madeAt;
}
