package it.paa.repository;


import it.paa.model.TrainingProgram;

import java.util.List;

public interface TrainingProgramRepository {
    List<TrainingProgram> findAll(Integer duration, String intensity);

    TrainingProgram findById(Long id);

    TrainingProgram save(TrainingProgram trainingProgram);

    TrainingProgram update(Long id, TrainingProgram trainingProgram);

    void deleteById(Long id);

    List<TrainingProgram> findByTrainingType(String trainingType);
}
