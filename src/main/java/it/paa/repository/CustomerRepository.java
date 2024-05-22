package it.paa.repository;

import it.paa.model.Customer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CustomerRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Customer> findAll(String name, String gender) {
        // Costruzione della stringa di base della query
        StringBuilder queryString = new StringBuilder("SELECT c FROM Customer c WHERE 1=1");

        // Aggiunta dei filtri se i parametri sono presenti
        if (name != null && !name.isEmpty() && !name.isBlank()) {
            queryString.append(" AND LOWER(c.name) = LOWER(:name)");
        }

        if (gender != null && !gender.isEmpty() && !gender.isBlank()) {
            queryString.append(" AND LOWER(c.gender) = LOWER(:gender)");
        }

        // Creazione della query
        TypedQuery<Customer> query = entityManager.createQuery(queryString.toString(), Customer.class);

        // Impostazione dei parametri se necessario
        if (name != null && !name.isEmpty() && !name.isBlank()) {
            query.setParameter("name", name.toLowerCase());
        }

        if (gender != null && !gender.isEmpty() && !gender.isBlank()) {
            query.setParameter("gender", gender.toLowerCase());
        }

        // Esecuzione della query e restituzione dei risultati
        return query.getResultList();
    }


    // recupero il customer dal db in base all'id passato
    public Customer findById(Long id) {

        return entityManager.find(Customer.class, id);
    }

    // salvo il customer nel db
    @Transactional
    public Customer save(Customer customer) {
        entityManager.persist(customer);
        return customer;
    }

    // aggiorno il customer nel db
    @Transactional
    public Customer update(Customer customer) {
        // effettuo il merge sul customer esistente
        return entityManager.merge(customer);
    }

    // elimino il customer dal db in base all'id passato
    @Transactional
    public Customer deleteById(Long id) {
        Customer customer = findById(id);
        if (customer != null) {
            entityManager.remove(customer);
        }
        return customer;
    }

}
