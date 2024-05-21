package it.paa.service;

import it.paa.model.Customer;
import it.paa.model.Trainer;
import it.paa.model.TrainingProgram;
import it.paa.repository.TrainerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class TrainerService implements TrainerRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Trainer> findAll(String name, String specialization) {

        // recupero la lista di trainer dal db
        List<Trainer> trainerList = entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class)
                .getResultList();

        // filtro la lista di trainer in base ai parametri passati
        if (name != null && !name.isEmpty() && !name.isBlank() && specialization != null && !specialization.isEmpty() && !specialization.isBlank()) {

            // se vengono passati entrambi i parametri filtro per entrambi
            trainerList = trainerList.stream()
                    .filter(trainer -> trainer.getName().equalsIgnoreCase(name) && trainer.getSpecialization().equalsIgnoreCase(specialization))
                    .collect(Collectors.toList());

        } else if (name != null && !name.isEmpty() && !name.isBlank()) {

            // se viene passato solo il name filtro per name
            trainerList = trainerList.stream()
                    .filter(trainer -> trainer.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());

        } else if (specialization != null && !specialization.isEmpty() && !specialization.isBlank()) {

            // se viene passata solo la specialization filtro per specialization
            trainerList = trainerList.stream()
                    .filter(trainer -> trainer.getSpecialization().equalsIgnoreCase(specialization))
                    .collect(Collectors.toList());
        }

        return trainerList;
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
    public Trainer update(Long id, Trainer trainer) {

        // controllo se il trainer esiste
        Trainer existingTrainer = findById(id);

        // dopo aver recuperato il trainer, aggiorno i dati
        existingTrainer.setName(trainer.getName());
        existingTrainer.setSurname(trainer.getSurname());
        existingTrainer.setSpecialization(trainer.getSpecialization());
        existingTrainer.setTrainingPrograms(trainer.getTrainingPrograms());
        existingTrainer.setWorkHours(trainer.getWorkHours());

        // effettuo il merge sull' trainer esistente
        return entityManager.merge(existingTrainer);
    }

    // elimino il trainer dal db in base all'id passato
    @Transactional
    public void deleteById(Long id) {
        Trainer trainer = findById(id);
        if (trainer != null) {
            entityManager.remove(trainer);
        }
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
