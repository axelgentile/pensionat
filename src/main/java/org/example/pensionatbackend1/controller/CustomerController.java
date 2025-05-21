package org.example.pensionatbackend1.controller;

import org.example.pensionatbackend1.dto.CustomerDto;
import org.example.pensionatbackend1.Models.Customer;
import org.example.pensionatbackend1.mapper.CustomerMapper;
import org.example.pensionatbackend1.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/all")
    public String getAllCustomers(Model model) {
        List<CustomerDto> customers = customerService.getAllCustomers()
                .stream()
                .map(CustomerMapper::toDto).toList();

        model.addAttribute("customers", customers);
        return "customers";
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(CustomerMapper.toDto(customer));
    }

    @GetMapping("/register")
    public String showRegisterCustomerForm(Model model) {
        if (!model.containsAttribute("customerDto")){
            model.addAttribute("customerDto", new CustomerDto());
        }

        return "register-customer";
    }

    @PostMapping("/register")
    public String registerCustomer(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                                   BindingResult bindingResult,
                                   Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("customerDto", customerDto);
            return "register-customer";
        }
        Customer customer = CustomerMapper.toEntity(customerDto);
        customerService.createCustomer(customer);
        redirectAttributes.addFlashAttribute("successMessage", "Kund har registrerats.");
        return "redirect:/customers/all";
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable long id,RedirectAttributes redirectAttributes) {
        customerService.deleteCustomerById(id);
        redirectAttributes.addFlashAttribute("deleteMessage", "Kund har tagits bort.");
        return "redirect:/customers/all";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        CustomerDto customerDto = CustomerMapper.toDto(customer);
        model.addAttribute("customerDto", customerDto);
        return "edit-customer";

    }
    @PostMapping("/edit")
    public String updateCustomer(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("customerDto", customerDto);
            return "edit-customer";
        }

        Customer updatedCustomer = CustomerMapper.toEntity(customerDto);
        customerService.updateCustomer(updatedCustomer);

        redirectAttributes.addFlashAttribute("successMessage", "Kunden har uppdaterats!");
        return "redirect:/customers/all";
    }


}
