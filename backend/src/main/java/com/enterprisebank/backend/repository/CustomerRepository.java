package com.enterprisebank.backend.repository;

import com.enterprisebank.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
