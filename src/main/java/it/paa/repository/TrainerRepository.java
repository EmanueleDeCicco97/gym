package it.paa.repository;

import it.paa.model.Customer;
import it.paa.model.Trainer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TrainerRepository {

    List<Trainer> findAll(String name, String specialization);

    Trainer findById(Long id);

    Trainer save(Trainer trainer);

    Trainer update(Long id, Trainer trainer);

    void deleteById(Long id);

    Map<Trainer, Set<Customer>> findTopTrainerWithMaxClients();

}
