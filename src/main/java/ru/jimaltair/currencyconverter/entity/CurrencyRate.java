package ru.jimaltair.currencyconverter.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@ToString
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double rate;

    @Column(nullable = false)
    private LocalDate date;

    @OneToOne(fetch = FetchType.LAZY)
    private Currency currency;
}
