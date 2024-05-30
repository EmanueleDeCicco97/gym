package it.paa.service;

import it.paa.model.Customer;
import it.paa.model.Trainer;
import it.paa.repository.TrainerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.*;

@ApplicationScoped
public class TrainerService {
    @Inject
    TrainerRepository trainerRepository;
    @Inject
    TrainingProgramService trainingProgramService;

    public List<Trainer> findAll(String name, String specialization) {
        return trainerRepository.findAll(name, specialization);
    }

    public Trainer findById(Long id) {
        Trainer trainer = trainerRepository.findById(id);
        if (trainer == null) {
            throw new NotFoundException("Trainer with id " + id + " not found");
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
        existingTrainer.setName(trainer.getName());

        existingTrainer.setSurname(trainer.getSurname());

        existingTrainer.setSpecialization(trainer.getSpecialization());

        existingTrainer.setWorkHours(trainer.getWorkHours());

        // effettuo il merge sull' trainer esistente
        return trainerRepository.update(existingTrainer);
    }

    // elimino il trainer dal db in base all'id passato
    @Transactional
    public Trainer deleteById(Long id) {

        if (trainingProgramService.isTrainerAssociated(id)) {
            throw new NotFoundException("Trainer with id " + id + " is associated with a training program and cannot be deleted");
        }

        Trainer trainer = trainerRepository.deleteById(id);

        if (trainer == null) {
            throw new NotFoundException("Trainer with id " + id + " not found");
        }
        return trainer;
    }

    public Map<Trainer, Set<Customer>> findTopTrainerWithMaxClients() {
        return trainerRepository.findTopTrainerWithMaxClients();
    }
}
