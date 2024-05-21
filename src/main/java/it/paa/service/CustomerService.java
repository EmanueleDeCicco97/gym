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

        // recupero la lista di customer dal db
        List<Customer> customers = entityManager.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();

        // filtro la lista di customer in base ai parametri passati
        if (name != null && !name.isEmpty() && !name.isBlank() && gender != null && !gender.isEmpty() && !gender.isBlank()) {

            // se vengono passati entrambi i parametri filtro per entrambi
            customers = customers.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(name) && c.getGender().equalsIgnoreCase(gender))
                    .collect(Collectors.toList());

        } else if (name != null && !name.isEmpty() && !name.isBlank()) {

            // se viene passato solo il name filtro per il name
            customers = customers.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());

        } else if (gender != null && !gender.isEmpty() && !gender.isBlank()) {

            // se viene passato solo il gender filtro per il gender
            customers = customers.stream()
                    .filter(c -> c.getGender().equalsIgnoreCase(gender))
                    .collect(Collectors.toList());
        }

        return customers;
    }

    // recupero il customer dal db in base all'id passato
    public Customer findById(Long id) {
        Customer customer = entityManager.find(Customer.class, id);
        if (customer == null) {
            throw new IllegalArgumentException("Customer with id " + id + " not found");
        }
        return customer;
    }

    // salvo il customer nel db
    @Transactional
    public Customer save(Customer customer) {
        entityManager.persist(customer);

        return customer;
    }

    // aggiorno il customer nel db
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
        return entityManager.merge(existingCustomer);
    }

    // elimino il customer dal db in base all'id passato
    @Transactional
    public void deleteById(Long id) {
        Customer customer = entityManager.find(Customer.class, id);
        if (customer != null) {
            entityManager.remove(customer);
        }
    }
}
