package it.paa.repository;

import it.paa.model.Customer;
import it.paa.model.Trainer;
import it.paa.model.TrainingProgram;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class TrainerRepository {
    @PersistenceContext
    EntityManager entityManager;

    public List<Trainer> findAll(String name, String specialization) {
        // Costruzione della stringa di base della query
        StringBuilder queryString = new StringBuilder("SELECT t FROM Trainer t WHERE 1=1");

        // Aggiunta dei filtri se i parametri sono presenti
        if (name != null && !name.isEmpty() && !name.isBlank()) {
            queryString.append(" AND LOWER(t.name) = :name");
        }

        if (specialization != null && !specialization.isEmpty() && !specialization.isBlank()) {
            queryString.append(" AND LOWER(t.specialization) = :specialization");
        }

        // Creazione della query
        TypedQuery<Trainer> query = entityManager.createQuery(queryString.toString(), Trainer.class);

        // Impostazione dei parametri se necessario
        if (name != null && !name.isEmpty() && !name.isBlank()) {
            query.setParameter("name", name.toLowerCase());
        }

        if (specialization != null && !specialization.isEmpty() && !specialization.isBlank()) {
            query.setParameter("specialization", specialization.toLowerCase());
        }

        // Esecuzione della query e restituzione dei risultati
        return query.getResultList();
    }


    // recupero il trainer dal db in base all'id passato
    public Trainer findById(Long id) {
        Trainer trainer = entityManager.find(Trainer.class, id);
        if (trainer == null) {
            throw new NotFoundException("Trainer with id " + id + " not found");
        }
        return trainer;
    }

    // salvo il trainer nel db
    @Transactional
    public Trainer save(Trainer trainer) {
        entityManager.persist(trainer);
        return trainer;
    }

    // aggiorno il trainer nel db
    @Transactional
    public Trainer update(Trainer trainer) {

        // effettuo il merge sull' trainer esistente
        return entityManager.merge(trainer);
    }

    // elimino il trainer dal db in base all'id passato
    @Transactional
    public Trainer deleteById(Long id) {
        Trainer trainer = findById(id);
        if (trainer != null) {
            entityManager.remove(trainer);
        }
        return trainer;
    }

    //query di partenza
    //SELECT t.id AS trainer_id, t.name AS trainer_name, c.id AS customer_id, c.name AS customer_name
    //FROM trainers t
    //INNER JOIN training_programs tp ON t.id = tp.trainer_id
    //INNER JOIN customers c ON tp.customer_id = c.id
    //GROUP BY t.id, t.name, c.id, c.name
    //ORDER BY COUNT(tp.id) DESC
    //LIMIT 3;

    //Esercitazione 2: Implementazione di un endpoint per trovare gli allenatori con il maggior numero di clienti e
    // visualizzare i dettagli dei clienti che seguono ciascun allenatore.
    public Map<Trainer, Set<Customer>> findTopTrainerWithMaxClients() {
        // Creo una mappa di risultato per memorizzare i trainer e i loro clienti
        Map<Trainer, Set<Customer>> mapResult = new LinkedHashMap<>();

        // Recupero la lista di trainer dal db
        List<Trainer> trainerList = entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class)
                .getResultList();

        trainerList.stream()
                // Filtro i trainer che hanno almeno un programma di allenamento
                .filter(trainer -> !trainer.getTrainingPrograms().isEmpty())
                // Ordino i trainer in base al numero di programmi di allenamento in ordine decrescente
                .sorted(Comparator.comparingInt(trainer -> -trainer.getTrainingPrograms().size()))
                // Limito il risultato a 3 quindi restituisco i primi 3 trainer con più clienti associati
                .limit(3)
                // Per ciascuno dei 3 trainer
                .forEach(trainer -> {
                    // Recupero una lista di clienti associati al trainer
                    Set<Customer> customers = trainer.getTrainingPrograms().stream()
                            .map(TrainingProgram::getAssociatedCustomer)
                            // Converto la lista di clienti in un set per eliminare duplicati
                            .collect(Collectors.toSet());
                    // Aggiungo il trainer e i suoi clienti alla mappa di risultato
                    mapResult.put(trainer, customers);
                });
        // Ritorno la mappa di risultato
        return mapResult;
    }

//    public Map<Trainer, Set<Customer>> findTopTrainerWithMaxClients() {
//        String query = "SELECT t, c " +
//                "FROM Trainer t " +
//                "JOIN t.trainingPrograms tp " +
//                "JOIN tp.associatedCustomer c " +
//                "GROUP BY t, c " +
//                "ORDER BY COUNT(c) DESC";
//
//        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query, Object[].class);
//
//
//        List<Object[]> resultList = typedQuery.getResultList();
//
//        Map<Trainer, Set<Customer>> mapResult = new LinkedHashMap<>();
//
//        for (Object[] result : resultList) {
//            Trainer trainer = (Trainer) result[0];
//            Customer customer = (Customer) result[1];
//
//            // Qui possiamo usare una nuova query per ottenere tutte le informazioni del cliente
//            // usando l'ID del cliente dall'oggetto Customer recuperato
//            String customerQuery = "SELECT c FROM Customer c WHERE c.id = :customerId";
//            TypedQuery<Customer> customerTypedQuery = entityManager.createQuery(customerQuery, Customer.class);
//            customerTypedQuery.setParameter("customerId", customer.getId());
//            List<Customer> customers = customerTypedQuery.getResultList();
//
//            // Aggiungiamo i clienti associati al Trainer corrispondente
//            mapResult.computeIfAbsent(trainer, k -> new HashSet<>()).addAll(customers);
//        }
//
//        return mapResult;
//    }


}
