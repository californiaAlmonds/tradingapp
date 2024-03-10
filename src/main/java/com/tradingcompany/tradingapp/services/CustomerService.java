package com.tradingcompany.tradingapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tradingcompany.tradingapp.exceptions.CustomerNotFoundException;
import com.tradingcompany.tradingapp.exceptions.StockNotFoundException;
import com.tradingcompany.tradingapp.models.Customer;
import com.tradingcompany.tradingapp.models.Stock;
import com.tradingcompany.tradingapp.repositories.CustomerRepository;

import jakarta.validation.Valid;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    public Customer createCustomer(Customer customer) {
        List<Stock> stocks = customer.getStocks();
        if (stocks != null) {
            for (Stock stock : stocks) {
                stock.setCustomer(customer);
            }
        }

        Customer savedCustomer = repository.save(customer);
        return savedCustomer;         }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existingCustomer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        // Update fields that should be allowed to change
        existingCustomer.setCustomerName(updatedCustomer.getCustomerName());
        existingCustomer.setBankAccountNumber(updatedCustomer.getBankAccountNumber());
        existingCustomer.setTradingAccountNumber(updatedCustomer.getTradingAccountNumber());
        existingCustomer.setCustomerAddress(updatedCustomer.getCustomerAddress());
        existingCustomer.setPanCardNumber(updatedCustomer.getPanCardNumber());
        existingCustomer.setAadharNumber(updatedCustomer.getAadharNumber());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        existingCustomer.setNotes(updatedCustomer.getNotes());

        // Save the updated customer
        Customer savedCustomer = repository.save(existingCustomer);
        return savedCustomer;
    }

    public void deleteCustomer(Long id) {
        repository.deleteById(id);
    }

    public Stock addStockToCustomer(Long customerId, @Valid Stock stock) {
        Customer customer = getCustomerById(customerId);
        customer.getStocks().add(stock);
        stock.setCustomer(customer);
        repository.save(customer);
        return stock;
    }

    public void updateStock(Long customerId, Long stockId, @Valid Stock updatedStock) {
        Customer customer = getCustomerById(customerId);
        for (Stock stock : customer.getStocks()) {
            if (stock.getId().equals(stockId)) {
                stock.setStockName(updatedStock.getStockName());
                stock.setPrice(updatedStock.getPrice());
                stock.setQuantity(updatedStock.getQuantity());
                stock.setStopLoss(updatedStock.getStopLoss());
                repository.save(customer);
                return;
            }
        }
        throw new StockNotFoundException("Stock not found with id: " + stockId);
    }

    public void removeStockFromCustomer(Long customerId, Long stockId) {
        Customer customer = getCustomerById(customerId);
        Stock stockToRemove = null;
        for (Stock stock : customer.getStocks()) {
            if (stock.getId().equals(stockId)) {
                stockToRemove = stock;
                break;
            }
        }
        if (stockToRemove != null) {
            customer.getStocks().remove(stockToRemove);
            stockToRemove.setCustomer(null);
            repository.save(customer);
        } else {
            throw new StockNotFoundException("Stock not found with id: " + stockId);
        }
    }
}

