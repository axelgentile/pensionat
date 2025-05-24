package org.example.pensionatbackend1;

import org.example.pensionatbackend1.controller.CustomerController;
import org.example.pensionatbackend1.dto.CustomerDto;
import org.example.pensionatbackend1.Models.Customer;
import org.example.pensionatbackend1.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void getAllCustomers_returnsViewWithCustomers() throws Exception {
        List<Customer> customers = List.of(new Customer(), new Customer());
        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/customers/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("customers"))
                .andExpect(model().attributeExists("customers"));
    }

    @Test
    void getCustomerById_returnsCustomerDto() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Anna");
        customer.setLastName("Andersson");
        when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Andersson"));
    }

    @Test
    void showRegisterCustomerForm_returnsViewWithCustomerDto() throws Exception {
        mockMvc.perform(get("/customers/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-customer"))
                .andExpect(model().attributeExists("customerDto"));
    }

    @Test
    void registerCustomer_withValidationErrors_returnsRegisterForm() throws Exception {
        mockMvc.perform(post("/customers/register")
                        .param("firstName", "") // Tomt namn → valideringsfel
                        .param("lastName", "")
                        .param("email", "")
                        .param("phoneNum", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("register-customer"))
                .andExpect(model().attributeExists("customerDto"));
    }

    @Test
    void registerCustomer_withValidInput_redirectsToAllCustomers() throws Exception {
        mockMvc.perform(post("/customers/register")
                        .param("firstName", "Anna")
                        .param("lastName", "Andersson")
                        .param("email", "anna@ex.se")
                        .param("phoneNum", "0701234567"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers/all"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    void deleteCustomer_withValidId_redirectsWithDeleteMessage() throws Exception {
        mockMvc.perform(post("/customers/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers/all"))
                .andExpect(flash().attributeExists("deleteMessage"));

        verify(customerService).deleteCustomerById(1L);
    }

    @Test
    void deleteCustomer_withException_redirectsWithErrorMessage() throws Exception {
        doThrow(new IllegalArgumentException("Kunden har bokningar.")).when(customerService).deleteCustomerById(2L);

        mockMvc.perform(post("/customers/delete/{id}", 2L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers/all"))
                .andExpect(flash().attributeExists("errorMessage"));

        verify(customerService).deleteCustomerById(2L);
    }

    @Test
    void showEditForm_returnsViewWithCustomerDto() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Anna");
        customer.setLastName("Andersson");
        when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/customers/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-customer"))
                .andExpect(model().attributeExists("customerDto"));
    }

    @Test
    void updateCustomer_withValidationErrors_returnsEditForm() throws Exception {
        mockMvc.perform(post("/customers/edit")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("email", "")
                        .param("phoneNum", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-customer"))
                .andExpect(model().attributeExists("customerDto"));
    }

    @Test
    void updateCustomer_withValidInput_redirectsWithSuccessMessage() throws Exception {
        mockMvc.perform(post("/customers/edit")
                        .param("id", "1")
                        .param("firstName", "Anna")
                        .param("lastName", "Andersson")
                        .param("email", "anna@ex.se")
                        .param("phoneNum", "0701234567"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers/all"))
                .andExpect(flash().attributeExists("successMessage"));
    }
}