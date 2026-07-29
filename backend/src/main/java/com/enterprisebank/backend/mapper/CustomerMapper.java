package com.enterprisebank.backend.mapper;

import com.enterprisebank.backend.dto.CustomerRequest;
import com.enterprisebank.backend.dto.CustomerResponse;
import com.enterprisebank.backend.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    // Convert DTO request to Entity
    public Customer toEntity(CustomerRequest request) {

        Customer customer = new Customer();

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        return customer;
    }


    // Convert Entity to DTO response
    public CustomerResponse toResponse(Customer customer) {

        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());

        return response;
    }
}
