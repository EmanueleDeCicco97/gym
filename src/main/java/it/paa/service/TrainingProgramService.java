package it.paa.service;

import it.paa.dto.TrainingProgramDto;
import it.paa.model.TrainingProgram;
import it.paa.repository.TrainingProgramRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

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

    public TrainingProgram findById(Long id) throws NotFoundException {

        TrainingProgram trainingProgram = trainingProgramRepository.findById(id);
        if (trainingProgram != null) {
            return trainingProgram;
        } else {
            throw new NotFoundException("Training Program with id " + id + " not found");
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

        existingTrainingProgram.setAssociatedCustomer(customerService.findById(trainingProgramDto.getCustomerId()));

        existingTrainingProgram.setAssociatedTrainer(trainerService.findById(trainingProgramDto.getTrainerId()));

        existingTrainingProgram.setTrainingType(trainingProgramDto.getTrainingType());

        existingTrainingProgram.setDuration(trainingProgramDto.getDuration());

        existingTrainingProgram.setIntensity(trainingProgramDto.getIntensity());

        trainingProgramRepository.update(existingTrainingProgram);
        return existingTrainingProgram;

    }

    @Transactional
    public TrainingProgram deleteById(Long id) {
        TrainingProgram trainingProgram = trainingProgramRepository.deleteById(id);

        if (trainingProgram == null) {
            throw new NotFoundException("Training Program with id " + id + " not found");
        }

        return trainingProgram;
    }

    public List<TrainingProgram> findByTrainingType(String trainingType) {
        return trainingProgramRepository.findByTrainingType(trainingType);
    }

    public boolean isCustomerAssociated(Long customerId) {

        return trainingProgramRepository.isCustomerAssociated(customerId);
    }

    public boolean isTrainerAssociated(Long trainerId) {

        return trainingProgramRepository.isTrainerAssociated(trainerId);
    }
}
