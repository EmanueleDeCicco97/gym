package it.paa.repository;


import it.paa.model.TrainingProgram;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TrainingProgramRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<TrainingProgram> findAll(Integer duration, String intensity) {
        // Costruzione della query di base
        String jpql = "SELECT tp FROM TrainingProgram tp";
        boolean hasDuration = duration != null;
        boolean hasIntensity = intensity != null && !intensity.isEmpty() && !intensity.isBlank();

        // Aggiunta delle condizioni di filtro se necessari
        if (hasDuration || hasIntensity) {
            jpql += " WHERE";
        }

        if (hasDuration) {
            jpql += " tp.duration = :duration";
        }

        if (hasDuration && hasIntensity) {
            jpql += " AND";
        }

        if (hasIntensity) {
            jpql += " LOWER(tp.intensity) = :intensity";
        }

        // Creazione della query tipizzata
        TypedQuery<TrainingProgram> query = entityManager.createQuery(jpql, TrainingProgram.class);

        // Impostazione dei parametri della query se presenti
        if (hasDuration) {
            query.setParameter("duration", duration);
        }

        if (hasIntensity) {
            query.setParameter("intensity", intensity.toLowerCase());
        }

        // Esecuzione della query e restituzione della lista
        return query.getResultList();
    }


    // recupero il training program dal db in base all'id passato
    public TrainingProgram findById(Long id) {
        return entityManager.find(TrainingProgram.class, id);
    }

    // salvo il training program nel db
    @Transactional
    public TrainingProgram save(TrainingProgram trainingProgram) {
        entityManager.persist(trainingProgram);
        return trainingProgram;
    }

    // controllo se esiste già un training program associato al cliente
    public boolean isCustomerAssociated(Long customerId) {
        // eseguo una query per controllare se esiste un TrainingProgram associato al cliente
        List<TrainingProgram> existingTrainingPrograms = entityManager.createQuery(
                        "SELECT tp FROM TrainingProgram tp " +
                                "WHERE tp.associatedCustomer.id = :customerId", TrainingProgram.class)
                .setParameter("customerId", customerId)
                .getResultList();

        // se esiste almeno un TrainingProgram associato al cliente, restituisci true
        return !existingTrainingPrograms.isEmpty();
    }

    public boolean isTrainerAssociated(Long trainerId) {
        List<TrainingProgram> existingTrainingPrograms = entityManager.createQuery(
                        "SELECT tp FROM TrainingProgram tp " +
                                "WHERE tp.associatedTrainer.id = :trainerId", TrainingProgram.class)
                .setParameter("trainerId", trainerId)
                .getResultList();

        return !existingTrainingPrograms.isEmpty();
    }

    // aggiorno il training program nel db
    @Transactional
    public void update(TrainingProgram trainingProgram) {
        entityManager.merge(trainingProgram);
    }

    // elimino il training program dal db in base all'id passato'
    @Transactional
    public TrainingProgram deleteById(Long id) {
        TrainingProgram trainingProgram = findById(id);
        if (trainingProgram != null) {
            entityManager.remove(trainingProgram);
        }
        return trainingProgram;
    }

    //•	Esercitazione 1: Creazione di una funzionalità per ottenere tutti i clienti che seguono un determinato programma di allenamento e
    // visualizzare i dettagli del programma e dell'allenatore.
    public List<TrainingProgram> findByTrainingType(String trainingType) {
        // Creazione della query con LOWER per il confronto case-insensitive
        List<TrainingProgram> trainingProgramList = entityManager.createQuery(
                        "SELECT tp FROM TrainingProgram tp WHERE LOWER(tp.trainingType) = LOWER(:trainingType)",
                        TrainingProgram.class)
                // Imposta il parametro "trainingType" in minuscolo
                .setParameter("trainingType", trainingType.toLowerCase())
                // Restituisce la lista di programmi di allenamento che corrispondono al tipo di allenamento specificato
                .getResultList();

        return trainingProgramList;
    }
}
