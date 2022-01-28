package ru.jimaltair.currencyconverter.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    @Id
    @Column
    private String numCode;

    @Column(nullable = false)
    private String charCode;

    @Column(nullable = false)
    private int nominal;

    @Column(nullable = false)
    private String name;
}
