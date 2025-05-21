package org.example.pensionatbackend1.service;

import org.example.pensionatbackend1.entity.Customer;
import org.example.pensionatbackend1.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository, CustomerRepository customerRepository) {
        this.repository = repository;
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());

    }

    public Customer createCustomer(Customer customer) {
        return repository.save(customer);
    }
}
