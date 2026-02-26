package com.henr.analise_credito.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.henr.analise_credito.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByCpf(String cpf);
}
