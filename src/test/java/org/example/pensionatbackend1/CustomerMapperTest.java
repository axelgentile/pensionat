package org.example.pensionatbackend1;

import org.example.pensionatbackend1.dto.CustomerDto;
import org.example.pensionatbackend1.Models.Customer;
import org.example.pensionatbackend1.mapper.CustomerMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    @Test
    void testToDto() {
        Customer customer = new Customer();
        customer.setId(8L);
        customer.setFirstName("Axel");
        customer.setLastName("Gentile");
        customer.setEmail("axel@test.com");
        customer.setPhoneNum("0721234567");

        CustomerDto dto = CustomerMapper.toDto(customer);

        assertEquals(8L, dto.getId());
        assertEquals("Axel", dto.getFirstName());
        assertEquals("Gentile", dto.getLastName());
        assertEquals("axel@test.com", dto.getEmail());
        assertEquals("0721234567", dto.getPhoneNum());
    }

    @Test
    void testToEntity() {
        CustomerDto dto = new CustomerDto();
        dto.setId(2L);
        dto.setFirstName("Kurt");
        dto.setLastName("Andersson");
        dto.setEmail("kurt@test.com");
        dto.setPhoneNum("0705551212");

        Customer customer = CustomerMapper.toEntity(dto);

        assertEquals(2L, customer.getId());
        assertEquals("Kurt", customer.getFirstName());
        assertEquals("Andersson", customer.getLastName());
        assertEquals("kurt@test.com", customer.getEmail());
        assertEquals("0705551212", customer.getPhoneNum());
    }
}
