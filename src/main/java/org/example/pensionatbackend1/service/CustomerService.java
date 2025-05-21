package org.example.pensionatbackend1.service;

import org.example.pensionatbackend1.entity.Customer;
import org.example.pensionatbackend1.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }
}
