package org.example.pensionatbackend1.controller;

import org.example.pensionatbackend1.entity.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return new ArrayList<>();
    }





}
