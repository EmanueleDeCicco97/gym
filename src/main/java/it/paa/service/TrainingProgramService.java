package it.paa.service;

import it.paa.model.TrainingProgram;
import it.paa.repository.TrainingProgramRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TrainingProgramService implements TrainingProgramRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<TrainingProgram> findAll(Integer duration, String intensity) {
        List<TrainingProgram> trainingProgramList = entityManager.createQuery("SELECT tp FROM TrainingProgram tp", TrainingProgram.class)
                .getResultList();
        if (duration != null && intensity != null && !intensity.isEmpty() && !intensity.isBlank()) {
            trainingProgramList = trainingProgramList.stream()
                    .filter(trainingProgram -> trainingProgram.getDuration() == duration && trainingProgram.getIntensity().equalsIgnoreCase(intensity))
                    .collect(Collectors.toList());
        } else if (duration != null) {
            trainingProgramList = trainingProgramList.stream()
                    .filter(trainingProgram -> trainingProgram.getDuration() == duration)
                    .collect(Collectors.toList());
        } else if (intensity != null && !intensity.isEmpty() && !intensity.isBlank()) {
            trainingProgramList = trainingProgramList.stream()
                    .filter(trainingProgram -> trainingProgram.getIntensity().equalsIgnoreCase(intensity))
                    .collect(Collectors.toList());
        }

        return trainingProgramList;
    }


    public TrainingProgram findById(Long id) {
        TrainingProgram trainingProgram = entityManager.find(TrainingProgram.class, id);
        if (trainingProgram == null) {
            throw new IllegalArgumentException("Training Program with id " + id + " not found");
        }
        return trainingProgram;
    }

    @Transactional
    public TrainingProgram save(TrainingProgram trainingProgram) {
        entityManager.persist(trainingProgram);
        return trainingProgram;
    }

    @Transactional
    public TrainingProgram update(Long id, TrainingProgram trainingProgram) {
        TrainingProgram existingTrainingProgram = findById(id);

        existingTrainingProgram.setTrainingType(trainingProgram.getTrainingType());
        existingTrainingProgram.setDuration(trainingProgram.getDuration());
        existingTrainingProgram.setIntensity(trainingProgram.getIntensity());

        return entityManager.merge(existingTrainingProgram);
    }

    @Transactional
    public void deleteById(Long id) {
        TrainingProgram trainingProgram = findById(id);
        if (trainingProgram != null) {
            entityManager.remove(trainingProgram);
        }
    }

}
