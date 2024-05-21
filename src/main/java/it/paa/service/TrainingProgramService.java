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

        // recupero la lista di training program dal db
        List<TrainingProgram> trainingProgramList = entityManager.createQuery("SELECT tp FROM TrainingProgram tp", TrainingProgram.class)
                .getResultList();

        // filtro la lista di training program in base ai parametri passati
        if (duration != null && intensity != null && !intensity.isEmpty() && !intensity.isBlank()) {

            //se vengono passati entrambi i parametri filtro per entrambi
            trainingProgramList = trainingProgramList.stream()
                    .filter(trainingProgram -> trainingProgram.getDuration() == duration && trainingProgram.getIntensity().equalsIgnoreCase(intensity))
                    .collect(Collectors.toList());

        } else if (duration != null) {

            // se viene passato solo la duration filtro per la duration
            trainingProgramList = trainingProgramList.stream()
                    .filter(trainingProgram -> trainingProgram.getDuration() == duration)
                    .collect(Collectors.toList());

        } else if (intensity != null && !intensity.isEmpty() && !intensity.isBlank()) {

            // se viene passato solo l'intensity filtro per l'intensity
            trainingProgramList = trainingProgramList.stream()
                    .filter(trainingProgram -> trainingProgram.getIntensity().equalsIgnoreCase(intensity))
                    .collect(Collectors.toList());
        }

        return trainingProgramList;
    }

    // recupero il training program dal db in base all'id passato
    public TrainingProgram findById(Long id) {
        TrainingProgram trainingProgram = entityManager.find(TrainingProgram.class, id);
        if (trainingProgram == null) {
            throw new IllegalArgumentException("Training Program with id " + id + " not found");
        }
        return trainingProgram;
    }

    // salvo il training program nel db
    @Transactional
    public TrainingProgram save(TrainingProgram trainingProgram) {
        entityManager.persist(trainingProgram);
        return trainingProgram;
    }

    // controllo se esiste già un training program associato al cliente
    public boolean isCustomerAssociated(Long customerId) {
        // eseguo una query per controllare se esiste un TrainingProgram associato al cliente
        List<TrainingProgram> existingTrainingPrograms = entityManager.createQuery(
                        "SELECT tp FROM TrainingProgram tp " +
                                "WHERE tp.associatedCustomer.id = :customerId", TrainingProgram.class)
                .setParameter("customerId", customerId)
                .getResultList();

        // se esiste almeno un TrainingProgram associato al cliente, restituisci true
        return !existingTrainingPrograms.isEmpty();
    }

    // aggiorno il training program nel db
    @Transactional
    public TrainingProgram update(Long id, TrainingProgram trainingProgram) {

        // controllo se il trainer esiste
        TrainingProgram existingTrainingProgram = findById(id);

        // dopo aver recuperato il training program, aggiorno i dati
        existingTrainingProgram.setTrainingType(trainingProgram.getTrainingType());
        existingTrainingProgram.setDuration(trainingProgram.getDuration());
        existingTrainingProgram.setIntensity(trainingProgram.getIntensity());

        // effettuo il merge sul training program esistente
        return entityManager.merge(existingTrainingProgram);
    }

    // elimino il training program dal db in base all'id passato'
    @Transactional
    public void deleteById(Long id) {
        TrainingProgram trainingProgram = findById(id);
        if (trainingProgram != null) {
            entityManager.remove(trainingProgram);
        }
    }


    // restituisco una lista di programmi di allenamento che corrispondono al tipo di allenamento specificato e visualizzo i dettagli
    public List<TrainingProgram> findByTrainingType(String trainingType) {
        // recupero tutti i programmi di allenamento che corrispondono al tipo di allenamento specificato
        List<TrainingProgram> trainingProgramList = entityManager.createQuery("SELECT tp FROM TrainingProgram tp where tp.trainingType = :trainingType", TrainingProgram.class)
                // imposto il parametro "trainingType" con il valore passato al metodo.
                .setParameter("trainingType", trainingType)
                // restituisco la lista di programmi di allenamento che corrispondono al tipo di allenamento specificato.
                .getResultList();
        return trainingProgramList;
    }
}
