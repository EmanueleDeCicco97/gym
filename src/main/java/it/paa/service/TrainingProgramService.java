package it.paa.service;

import it.paa.dto.TrainingProgramDto;
import it.paa.model.TrainingProgram;
import it.paa.repository.TrainingProgramRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TrainingProgramService {

    @Inject
    TrainingProgramRepository trainingProgramRepository;
    @Inject
    CustomerService customerService;
    @Inject
    TrainerService trainerService;

    public List<TrainingProgram> findAll(Integer duration, String intensity) {
        return trainingProgramRepository.findAll(duration, intensity);
    }

    public TrainingProgram findById(Long id) throws IllegalArgumentException {

        TrainingProgram trainingProgram = trainingProgramRepository.findById(id);
        if (trainingProgram != null) {
            return trainingProgram;
        } else {
            throw new IllegalArgumentException("Training Program with id " + id + " not found");
        }
    }

    @Transactional
    public TrainingProgram save(TrainingProgram trainingProgram) {
        trainingProgramRepository.save(trainingProgram);
        return trainingProgram;
    }

    @Transactional
    public TrainingProgram update(Long id, TrainingProgramDto trainingProgramDto) {
        TrainingProgram existingTrainingProgram = findById(id);

        // dopo aver recuperato il training program, aggiorno i dati

        // se il customer id non è null aggiorno il customer
        if (trainingProgramDto.getCustomerId() != null) {
            existingTrainingProgram.setAssociatedCustomer(customerService.findById(trainingProgramDto.getCustomerId()));
        }
        // se il trainer id non è null aggiorno il trainer
        if (trainingProgramDto.getTrainerId() != null) {
            existingTrainingProgram.setAssociatedTrainer(trainerService.findById(trainingProgramDto.getTrainerId()));
        }

        // se il training type non è vuoto aggiorno il training type
        if (!trainingProgramDto.getTrainingType().isEmpty() && !trainingProgramDto.getTrainingType().isBlank()) {
            existingTrainingProgram.setTrainingType(trainingProgramDto.getTrainingType());
        }
        // se la durata non è nulla aggiorno la durata
        if (trainingProgramDto.getDuration() != null) {
            existingTrainingProgram.setDuration(trainingProgramDto.getDuration());
        }
        // se l'intensità non è vuota aggiorno l'intensità
        if (!trainingProgramDto.getIntensity().isEmpty() && !trainingProgramDto.getIntensity().isBlank()) {
            existingTrainingProgram.setIntensity(trainingProgramDto.getIntensity());
        }
        trainingProgramRepository.update(existingTrainingProgram);
        return existingTrainingProgram;

    }

    @Transactional
    public TrainingProgram deleteById(Long id) {
        TrainingProgram trainingProgram = trainingProgramRepository.deleteById(id);

        if (trainingProgram == null) {
            throw new IllegalArgumentException("Training Program with id " + id + " not found");
        }

        return trainingProgram;
    }

    public List<TrainingProgram> findByTrainingType(String trainingType) {
        return trainingProgramRepository.findByTrainingType(trainingType);
    }

    public boolean isCustomerAssociated(Long customerId) {

        return trainingProgramRepository.isCustomerAssociated(customerId);
    }
}
