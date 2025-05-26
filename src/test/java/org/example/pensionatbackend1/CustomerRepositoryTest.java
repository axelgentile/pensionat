package org.example.pensionatbackend1;

import org.example.pensionatbackend1.Models.Customer;
import org.example.pensionatbackend1.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Save a Customer and find it by ID")
    void saveAndFindById() {
        // Arrange
        Customer c = new Customer(
                null,
                "Alice",
                "Andersson",
                "alice@example.com",
                "0701234567"
        );

        // Act
        Customer saved    = customerRepository.save(c);
        Optional<Customer> fetched = customerRepository.findById(saved.getId());

        // Assert
        assertThat(fetched).isPresent();
        Customer cust = fetched.get();
        assertThat(cust.getFirstName()).isEqualTo("Alice");
        assertThat(cust.getLastName()).isEqualTo("Andersson");
        assertThat(cust.getEmail()).isEqualTo("alice@example.com");
        assertThat(cust.getPhoneNum()).isEqualTo("0701234567");
    }

    @Test
    @DisplayName("findAll returns all saved Customers")
    void findAllReturnsAll() {
        // Arrange
        Customer c1 = new Customer(null, "Bob",  "Bengtsson", "bob@example.com",  "0701111111");
        Customer c2 = new Customer(null, "Carl", "Carlsson",  "carl@example.com", "0702222222");
        customerRepository.saveAll(List.of(c1, c2));

        // Act
        List<Customer> all = customerRepository.findAll();

        // Assert
        assertThat(all)
                .hasSize(2)
                .extracting(Customer::getFirstName)
                .containsExactlyInAnyOrder("Bob", "Carl");
    }

    @Test
    @DisplayName("deleteById removes the Customer")
    void deleteByIdRemoves() {
        // Arrange
        Customer c = customerRepository.save(
                new Customer(null, "Dagny", "Dahl", "dagny@example.com", "0703333333")
        );
        Long id = c.getId();

        // Act
        customerRepository.deleteById(id);
        Optional<Customer> after = customerRepository.findById(id);

        // Assert
        assertThat(after).isEmpty();
    }
}
