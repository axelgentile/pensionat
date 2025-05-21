package org.example.pensionatbackend1.mapper;

import org.example.pensionatbackend1.dto.CustomerDto;
import org.example.pensionatbackend1.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public static CustomerDto toDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        return dto;
    }

    public static Customer toEntity(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        return customer;
    }
}
