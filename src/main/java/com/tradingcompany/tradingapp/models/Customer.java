package com.tradingcompany.tradingapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String customerName;

    @NotNull
    private Long bankAccountNumber;

    @NotNull
    private Long tradingAccountNumber;

    @Embedded
    private Address customerAddress;

    @NotBlank
    @Size(max = 999999999)
    private String panCardNumber;

    @NotBlank
    @Size(max = 999999999)
    private String aadharNumber;
    
    private String notes;
    @PositiveOrZero
    private long phoneNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stocks = new ArrayList<>();


    public void addStock(Stock stock) {
        stocks.add(stock);
        stock.setCustomer(this); // set the customer reference in the stock
    }

    public void removeStock(Stock stock) {
        stocks.remove(stock);
        stock.setCustomer(null); // remove the customer reference in the stock
    }
}
