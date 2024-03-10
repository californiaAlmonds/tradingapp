package com.tradingcompany.tradingapp;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tradingcompany.tradingapp.models.Customer;
import com.tradingcompany.tradingapp.repositories.CustomerRepository;
import com.tradingcompany.tradingapp.services.CustomerService;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TradingappApplicationTests {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Order(1)
    public void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setCustomerName("John Doe");
        customer.setBankAccountNumber(123456789L);
        customer.setTradingAccountNumber(987654321L);
        customer.setPanCardNumber("ABCDE1234F");
        customer.setAadharNumber("123412341234");
        customer.setNotes("Some notes");
        customer.setPhoneNumber(1234567890L);

        Customer createdCustomer = customerService.createCustomer(customer);

        Assertions.assertNotNull(createdCustomer);
        Assertions.assertEquals("John Doe", createdCustomer.getCustomerName());
        Assertions.assertEquals(123456789, createdCustomer.getBankAccountNumber());
        Assertions.assertEquals(987654321, createdCustomer.getTradingAccountNumber());
        Assertions.assertEquals("ABCDE1234F", createdCustomer.getPanCardNumber());
        Assertions.assertEquals("123412341234", createdCustomer.getAadharNumber());
        Assertions.assertEquals("Some notes", createdCustomer.getNotes());
        Assertions.assertEquals(1234567890L, createdCustomer.getPhoneNumber());

        // Save the created customer for future tests
        customerRepository.save(createdCustomer);
    }

    @Test
    @Order(2)
    public void testUpdateCustomer() {
        Customer customer = customerRepository.findById(1L).orElseThrow();

        // Update customer details
        customer.setCustomerName("Jane Doe");
        customer.setBankAccountNumber(987654321L);
        // Update other fields...
        customer.setNotes("Updated notes");
        customer.setPhoneNumber(1111111111L);

        Customer updatedCustomer = customerService.updateCustomer(customer.getId(), customer);

        Assertions.assertNotNull(updatedCustomer);
        Assertions.assertEquals("Jane Doe", updatedCustomer.getCustomerName());
        Assertions.assertEquals(987654321, updatedCustomer.getBankAccountNumber());
        Assertions.assertEquals("Updated notes", updatedCustomer.getNotes());
        Assertions.assertEquals(3, updatedCustomer.getPhoneNumber());
    }

    @Test
    @Order(3)
    public void testGetCustomerById() {
        Customer customer = customerRepository.findById(3L).orElseThrow();

        Long customerId = customer.getId();
        Customer fetchedCustomer = customerService.getCustomerById(customerId);

        Assertions.assertNotNull(fetchedCustomer);
        Assertions.assertEquals("Jane Doe", fetchedCustomer.getCustomerName());
        Assertions.assertEquals(987654321, fetchedCustomer.getBankAccountNumber());
    }

    @Test
    @Order(4)
    public void testDeleteCustomer() {
        Customer customer = customerRepository.findById(3L).orElseThrow();

        Long customerId = customer.getId();
        customerService.deleteCustomer(customerId);

        Optional<Customer> deletedCustomer = customerRepository.findById(customerId);
        Assertions.assertFalse(deletedCustomer.isPresent(), "Customer should be deleted");
    }
}
