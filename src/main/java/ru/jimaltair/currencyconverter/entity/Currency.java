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
    @Column
    private int numCode;

    @Column(nullable = false)
    private String charCode;

    @Column(nullable = false)
    private int nominal;

    @Column(nullable = false)
    private String name;
}
