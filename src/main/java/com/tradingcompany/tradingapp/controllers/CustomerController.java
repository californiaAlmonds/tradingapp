package com.tradingcompany.tradingapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tradingcompany.tradingapp.dtos.ResponseWrapper;
import com.tradingcompany.tradingapp.models.Customer;
import com.tradingcompany.tradingapp.models.Stock;
import com.tradingcompany.tradingapp.services.CustomerService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<Customer>>> getAllCustomers() {
        List<Customer> customers = service.getAllCustomers();
        ResponseWrapper<List<Customer>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", customers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Customer>> getCustomerById(@PathVariable Long id) {
        Customer customer = service.getCustomerById(id);
        ResponseWrapper<Customer> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", customer);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Customer>> createCustomer(@Valid @RequestBody Customer customer) {
        Customer createdCustomer = service.createCustomer(customer);
        ResponseWrapper<Customer> response = new ResponseWrapper<>(HttpStatus.CREATED.value(), "Customer created", createdCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Customer>> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        Customer updated = service.updateCustomer(id, updatedCustomer);
        ResponseWrapper<Customer> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Customer updated", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteCustomer(@PathVariable Long id) {
        service.deleteCustomer(id);
        ResponseWrapper<Void> response = new ResponseWrapper<>(HttpStatus.NO_CONTENT.value(), "Customer deleted", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PostMapping("/{customerId}/stocks")
    public ResponseEntity<ResponseWrapper<Stock>> addStockToCustomer(@PathVariable Long customerId,
                                                     @Valid @RequestBody Stock stock) {
        Stock addedStock = service.addStockToCustomer(customerId, stock);
        ResponseWrapper<Stock> response = new ResponseWrapper<>(HttpStatus.CREATED.value(), "Stock added to customer", addedStock);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{customerId}/stocks/{stockId}")
    public ResponseEntity<ResponseWrapper<Void>> updateStock(@PathVariable Long customerId,
                                            @PathVariable Long stockId,
                                            @Valid @RequestBody Stock updatedStock) {
        service.updateStock(customerId, stockId, updatedStock);
        ResponseWrapper<Void> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Stock updated", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{customerId}/stocks/{stockId}")
    public ResponseEntity<ResponseWrapper<Void>> removeStockFromCustomer(@PathVariable Long customerId,
                                                        @PathVariable Long stockId) {
        service.removeStockFromCustomer(customerId, stockId);
        ResponseWrapper<Void> response = new ResponseWrapper<>(HttpStatus.NO_CONTENT.value(), "Stock removed from customer", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
