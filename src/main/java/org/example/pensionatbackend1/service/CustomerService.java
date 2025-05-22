package org.example.pensionatbackend1.service;

import org.example.pensionatbackend1.Models.Customer;
import org.example.pensionatbackend1.repository.BookingRepository;
import org.example.pensionatbackend1.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final BookingRepository bookingRepository;

    public CustomerService(CustomerRepository repository, BookingRepository bookingRepository) {
        this.repository = repository;
        this.bookingRepository = bookingRepository;
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerById(long id) {
        return repository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public void createCustomer(Customer customer) {
        repository.save(customer);
    }
    public void deleteCustomerById(Long id){
        boolean hasBooking = bookingRepository.existsByCustomerId(id);
        if (hasBooking){
            throw new IllegalArgumentException("Kunden har bokningar.");
        }
        repository.deleteById(id);
    }
    public void updateCustomer(Customer updateCustomer){
        Customer existingCustomer = repository.findById(updateCustomer.getId()).orElseThrow(() -> new RuntimeException("Kunden hittades inte."));
        existingCustomer.setFirstName(updateCustomer.getFirstName());
        existingCustomer.setLastName(updateCustomer.getLastName());
        existingCustomer.setEmail(updateCustomer.getEmail());
        existingCustomer.setPhoneNum(updateCustomer.getPhoneNum());
        repository.save(existingCustomer);

    }
}
