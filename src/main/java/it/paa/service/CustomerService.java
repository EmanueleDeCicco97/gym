package it.paa.service;

import it.paa.model.Customer;
import it.paa.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;

    public List<Customer> findAll(String name, String gender) {
        return customerRepository.findAll(name, gender);
    }

    public Customer findById(Long id) {
        Customer customer = customerRepository.findById(id);
        if (customer == null) {
            throw new NotFoundException("Customer with id " + id + " not found");
        }
        return customer;
    }

    @Transactional
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer update(Long id, Customer customer) {
        // controllo se il customer esiste
        Customer existingCustomer = findById(id);

        // dopo aver recuperato il customer, aggiorno i dati
        existingCustomer.setName(customer.getName());

        existingCustomer.setSurname(customer.getSurname());

        existingCustomer.setDateOfBirth(customer.getDateOfBirth());

        existingCustomer.setGender(customer.getGender());

        existingCustomer.setActiveSubscription(customer.getActiveSubscription());


        // effettuo il merge sul customer esistente
        return customerRepository.update(existingCustomer);
    }

    @Transactional
    public Customer deleteById(Long id) {
        Customer customer = customerRepository.deleteById(id);
        if (customer == null) {
            throw new NotFoundException("Customer with id " + id + " not found");
        }
        return customer;
    }


}
