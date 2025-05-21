package org.example.pensionatbackend1.repository;
import org.example.pensionatbackend1.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
