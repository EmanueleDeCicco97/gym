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
        List<Trainer> trainerList = entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class)
                .getResultList();
        if (name != null && !name.isEmpty() && !name.isBlank() && specialization != null && !specialization.isEmpty() && !specialization.isBlank()) {
            trainerList = trainerList.stream()
                    .filter(trainer -> trainer.getName().equalsIgnoreCase(name) && trainer.getSpecialization().equalsIgnoreCase(specialization))
                    .collect(Collectors.toList());
        } else if (name != null && !name.isEmpty() && !name.isBlank()) {
            trainerList = trainerList.stream()
                    .filter(trainer -> trainer.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
        } else if (specialization != null && !specialization.isEmpty() && !specialization.isBlank()) {
            trainerList = trainerList.stream()
                    .filter(trainer -> trainer.getSpecialization().equalsIgnoreCase(specialization))
                    .collect(Collectors.toList());
        }

        return trainerList;
    }

    public Trainer findById(Long id) {
        Trainer trainer = entityManager.find(Trainer.class, id);
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer with id " + id + " not found");
        }
        return trainer;
    }

    @Transactional
    public Trainer save(Trainer trainer) {
        entityManager.persist(trainer);
        return trainer;
    }

    @Transactional
    public Trainer update(Long id, Trainer trainer) {
        Trainer existingTrainer = findById(id);

        existingTrainer.setName(trainer.getName());
        existingTrainer.setSurname(trainer.getSurname());
        existingTrainer.setSpecialization(trainer.getSpecialization());
        existingTrainer.setTrainingPrograms(trainer.getTrainingPrograms());
        existingTrainer.setWorkHours(trainer.getWorkHours());

        return entityManager.merge(existingTrainer);
    }

    @Transactional
    public void deleteById(Long id) {
        Trainer trainer = findById(id);
        if (trainer != null) {
            entityManager.remove(trainer);
        }
    }

    public Map<Trainer, Set<Customer>> findTopTrainerWithMaxClients() {
        Map<Trainer, Set<Customer>> mapResult = new LinkedHashMap<>();

        List<Trainer> trainerList = entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class)
                .getResultList();

        trainerList.stream()
                .sorted(Comparator.comparingInt(trainer -> -trainer.getTrainingPrograms().size()))
                .limit(3)
                .forEach(trainer -> {
                    Set<Customer> customers = trainer.getTrainingPrograms().stream()
                            .map(TrainingProgram::getAssociatedCustomer)
                            .collect(Collectors.toSet());
                    mapResult.put(trainer, customers);
                });

        return mapResult;
    }

}
