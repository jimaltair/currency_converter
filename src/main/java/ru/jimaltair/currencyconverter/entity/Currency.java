package ru.jimaltair.currencyconverter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    @Id
    @Column(name = "num_code", nullable = false)
    private int numCode;

    @Column(name = "char_code", nullable = false)
    private String charCode;

    @Column(name = "nominal", nullable = false)
    private int nominal;

    @Column(name = "name", nullable = false)
    private String name;
}
