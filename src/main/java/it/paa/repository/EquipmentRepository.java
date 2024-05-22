package it.paa.repository;

import it.paa.model.Equipment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EquipmentRepository {
    @PersistenceContext
    EntityManager entityManager;

    public List<Equipment> findAll(String name, Integer availability) {
        // Costruzione della stringa di base della query
        StringBuilder queryString = new StringBuilder("SELECT e FROM Equipment e WHERE 1=1");

        // Aggiunta dei filtri se i parametri sono presenti
        if (name != null && !name.isEmpty() && !name.isBlank()) {
            queryString.append(" AND LOWER(e.name) = :name");
        }

        if (availability != null) {
            queryString.append(" AND e.availability = :availability");
        }

        // Creazione della query
        TypedQuery<Equipment> query = entityManager.createQuery(queryString.toString(), Equipment.class);

        // Impostazione dei parametri se necessario
        if (name != null && !name.isEmpty() && !name.isBlank()) {
            query.setParameter("name", name.toLowerCase());
        }

        if (availability != null) {
            query.setParameter("availability", availability);
        }

        // Esecuzione della query e restituzione dei risultati
        return query.getResultList();
    }


    // recupero l'equipment dal db in base all'id passato
    public Equipment findById(Long id) {

        return entityManager.find(Equipment.class, id);
    }

    // salvo l'equipment nel db
    @Transactional
    public Equipment save(Equipment equipment) {
        entityManager.persist(equipment);
        return equipment;
    }

    // aggiorno l'equipment nel db
    @Transactional
    public Equipment update(Equipment equipment) {
        //effettuo il merge sull' equipment esistente
        return entityManager.merge(equipment);
    }

    // elimino l'equipment dal db in base all'id passato
    @Transactional
    public Equipment deleteById(Long id) {
        Equipment equipment = findById(id);
        if (equipment != null) {
            entityManager.remove(equipment);
        }
        return equipment;
    }

}
