package it.paa.repository;

import it.paa.model.Customer;
import it.paa.model.Trainer;
import it.paa.model.TrainingProgram;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

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
            throw new IllegalArgumentException("Trainer with id " + id + " not found");
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

    // restituisco una mappa dei primi 3 trainer con più clienti associati.
    public Map<Trainer, Set<Customer>> findTopTrainerWithMaxClients() {
        // creo una mappa di risultato per memorizzare i trainer e i loro clienti
        Map<Trainer, Set<Customer>> mapResult = new LinkedHashMap<>();


        // recupero la lista di trainer dal db
        List<Trainer> trainerList = entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class)
                .getResultList();

        trainerList.stream()
                // ordino i trainer in base al numero di programmi di allenamento in ordine decrescente.
                .sorted(Comparator.comparingInt(trainer -> -trainer.getTrainingPrograms().size()))
                // limito il risultato a 3 quindi restituisco i primi 3 trainer con più clienti associati.
                .limit(3)
                //per ciascuno dei 3 trainer
                .forEach(trainer -> {
                    // recupero una lista di clienti associati al trainer
                    Set<Customer> customers = trainer.getTrainingPrograms().stream()
                            .map(TrainingProgram::getAssociatedCustomer)
                            // converto la lista di clienti in un set per eliminare duplicati.
                            .collect(Collectors.toSet());
                    // aggiungo il trainer e i suoi clienti alla mappa di risultato.
                    mapResult.put(trainer, customers);
                });
        // ritorno la mappa di risultato.
        return mapResult;
    }


}
