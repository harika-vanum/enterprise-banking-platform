package com.enterprisebank.backend.service.Impl;

import com.enterprisebank.backend.dto.CustomerRequest;
import com.enterprisebank.backend.dto.CustomerResponse;
import com.enterprisebank.backend.entity.Customer;
import com.enterprisebank.backend.mapper.CustomerMapper;
import com.enterprisebank.backend.repository.CustomerRepository;
import com.enterprisebank.backend.service.CustomerService;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = customerMapper.toEntity(request);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        return customerMapper.toResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        Customer updatedCustomer = customerRepository.save(customer);

        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customerRepository.delete(customer);
    }
}
