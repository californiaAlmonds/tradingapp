package com.tradingcompany.tradingapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tradingcompany.tradingapp.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

