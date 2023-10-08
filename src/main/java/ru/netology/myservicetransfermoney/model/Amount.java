package ru.netology.myservicetransfermoney.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Amount {

    private Integer value;
    private String currency;


    @Override
    public String toString() {
        return value + " " + currency;

    }
}
