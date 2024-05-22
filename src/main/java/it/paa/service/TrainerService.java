package it.paa.service;

import it.paa.model.Customer;
import it.paa.model.Trainer;
import it.paa.model.TrainingProgram;
import it.paa.repository.TrainerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class TrainerService {
    @Inject
    TrainerRepository trainerRepository;

    public List<Trainer> findAll(String name, String specialization) {
        return trainerRepository.findAll(name, specialization);
    }

    public Trainer findById(Long id) {
        Trainer trainer = trainerRepository.findById(id);
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer with id " + id + " not found");
        }
        return trainer;
    }

    public Trainer save(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    // aggiorno il trainer nel db
    @Transactional
    public Trainer update(Long id, Trainer trainer) {

        // controllo se il trainer esiste
        Trainer existingTrainer = findById(id);

        // dopo aver recuperato il trainer, aggiorno i dati
        // se il nome non è vuoto lo aggiorno
        if (trainer.getName() != null && !trainer.getName().isEmpty() && !trainer.getName().isBlank()) {
            existingTrainer.setName(trainer.getName());
        }
        // se il cognome non è vuoto lo aggiorno
        if (trainer.getSurname() != null && !trainer.getSurname().isEmpty() && !trainer.getSurname().isBlank()) {
            existingTrainer.setSurname(trainer.getSurname());
        }
        // se la specializzazione non è vuota la aggiorno
        if (trainer.getSpecialization() != null && !trainer.getSpecialization().isEmpty() && !trainer.getSpecialization().isBlank()) {
            existingTrainer.setSpecialization(trainer.getSpecialization());
        }
        // se le ore di lavoro non sono vuote le aggiorno
        if (trainer.getWorkHours() != null && trainer.getWorkHours() > 0) {
            existingTrainer.setWorkHours(trainer.getWorkHours());
        }

        // effettuo il merge sull' trainer esistente
        return trainerRepository.update(existingTrainer);
    }

    // elimino il trainer dal db in base all'id passato
    @Transactional
    public Trainer deleteById(Long id) {
        Trainer trainer = trainerRepository.deleteById(id);
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer with id " + id + " not found");
        }
        return trainer;
    }

    public Map<Trainer, Set<Customer>> findTopTrainerWithMaxClients() {
        return trainerRepository.findTopTrainerWithMaxClients();
    }
}
