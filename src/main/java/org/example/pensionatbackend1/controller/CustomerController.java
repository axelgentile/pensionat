package org.example.pensionatbackend1.controller;

import org.apache.coyote.Response;
import org.example.pensionatbackend1.dto.CustomerDto;
import org.example.pensionatbackend1.entity.Customer;
import org.example.pensionatbackend1.mapper.CustomerMapper;
import org.example.pensionatbackend1.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/all")
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers().stream().map(CustomerMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(CustomerMapper.toDto(customer));
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerDto> registerCustomer(@RequestBody CustomerDto customerDto) {

        Customer customer = CustomerMapper.toEntity(customerDto);
        Customer saved = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerMapper.toDto(saved));
    }
}
