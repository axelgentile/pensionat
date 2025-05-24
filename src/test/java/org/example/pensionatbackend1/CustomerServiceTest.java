package org.example.pensionatbackend1;

import org.example.pensionatbackend1.Models.Customer;
import org.example.pensionatbackend1.repository.BookingRepository;
import org.example.pensionatbackend1.repository.CustomerRepository;
import org.example.pensionatbackend1.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getAllCustomers_returnsAllCustomers() {
        Customer c1 = new Customer();
        Customer c2 = new Customer();
        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById_existingId_returnsCustomer() {
        Customer customer = new Customer();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1L);

        assertNotNull(result);
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_nonExistingId_throwsException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> customerService.getCustomerById(99L));
    }

    @Test
    void createCustomer_savesCustomer() {
        Customer customer = new Customer();
        customerService.createCustomer(customer);

        verify(customerRepository).save(customer);
    }

    @Test
    void deleteCustomerById_noBookings_deletesCustomer() {
        when(bookingRepository.existsByCustomerId(1L)).thenReturn(false);

        customerService.deleteCustomerById(1L);

        verify(customerRepository).deleteById(1L);
    }

    @Test
    void deleteCustomerById_withBookings_throwsException() {
        when(bookingRepository.existsByCustomerId(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> customerService.deleteCustomerById(1L));
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateCustomer_existingCustomer_updatesFieldsAndSaves() {
        Customer existing = new Customer();
        existing.setId(1L);
        existing.setFirstName("Old");
        existing.setLastName("Name");
        existing.setEmail("old@mail.com");
        existing.setPhoneNum("123");

        Customer update = new Customer();
        update.setId(1L);
        update.setFirstName("New");
        update.setLastName("Name");
        update.setEmail("new@mail.com");
        update.setPhoneNum("456");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));

        customerService.updateCustomer(update);

        // Kontrollera att fälten har uppdaterats
        assertEquals("New", existing.getFirstName());
        assertEquals("Name", existing.getLastName());
        assertEquals("new@mail.com", existing.getEmail());
        assertEquals("456", existing.getPhoneNum());
        verify(customerRepository).save(existing);
    }

    @Test
    void updateCustomer_nonExistingCustomer_throwsException() {
        Customer update = new Customer();
        update.setId(99L);
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customerService.updateCustomer(update));
    }
}