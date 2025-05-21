package org.example.pensionatbackend1.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @GeneratedValue
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;

}
