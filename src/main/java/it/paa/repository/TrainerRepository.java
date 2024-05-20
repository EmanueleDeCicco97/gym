package it.paa.repository;

import it.paa.model.Trainer;

import java.util.List;

public interface TrainerRepository {

    List<Trainer> findAll(String name, String specialization);

    Trainer findById(Long id);

    Trainer save(Trainer trainer);

    Trainer update(Long id, Trainer trainer);

    void deleteById(Long id);

}
