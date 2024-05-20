package it.paa.service;

import it.paa.model.Customer;
import it.paa.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerService implements CustomerRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Customer> findAll(String name, String gender) {

        List<Customer> customers = entityManager.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
        if (name != null && !name.isEmpty() && !name.isBlank() && gender != null && !gender.isEmpty() && !gender.isBlank()) {

            customers = customers.stream().filter(c -> c.getName().equalsIgnoreCase(name) && c.getGender().equalsIgnoreCase(gender)).collect(Collectors.toList());

        } else if (name != null && !name.isEmpty() && !name.isBlank()) {

            customers = customers.stream().filter(c -> c.getName().equalsIgnoreCase(name)).collect(Collectors.toList());

        } else if (gender != null && !gender.isEmpty() && !gender.isBlank()) {

            customers = customers.stream().filter(c -> c.getGender().equalsIgnoreCase(gender)).collect(Collectors.toList());
        }

        return customers;
    }

    public Customer findById(Long id) {
        Customer customer = entityManager.find(Customer.class, id);
        if (customer == null) {
            throw new IllegalArgumentException("Customer with id " + id + " not found");
        }
        return customer;
    }

    @Transactional
    public Customer save(Customer customer) {
        entityManager.persist(customer);
        return customer;
    }

    @Transactional
    public Customer update(Long id, Customer customer) {
        // Controllo se il cliente esiste
        Customer existingCustomer = findById(id);

        if (existingCustomer == null) {
            throw new IllegalArgumentException("Customer with id " + customer.getId() + " not found");
        }

        // Effettua l'aggiornamento del cliente esistente
        existingCustomer.setName(customer.getName());
        existingCustomer.setSurname(customer.getSurname());
        existingCustomer.setDateOfBirth(customer.getDateOfBirth());
        existingCustomer.setGender(customer.getGender());
        existingCustomer.setActiveSubscription(customer.getActiveSubscription());

        return entityManager.merge(existingCustomer);
    }

    @Transactional
    public void deleteById(Long id) {
        Customer customer = entityManager.find(Customer.class, id);
        if (customer != null) {
            entityManager.remove(customer);
        }
    }
}
