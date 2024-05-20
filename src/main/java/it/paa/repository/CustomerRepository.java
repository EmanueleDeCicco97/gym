package it.paa.repository;

import it.paa.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    List<Customer> findAll(String name, String gender);
    Customer findById(Long id);
    Customer save(Customer customer);
    Customer update(Long id, Customer customer);
    void deleteById(Long id);

}
