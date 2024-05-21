package it.paa.service;

import it.paa.model.Equipment;
import it.paa.repository.EquipmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EquipmentService implements EquipmentRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Equipment> findAll(String name, Integer availability) {

        // recupero la lista di equipment dal db
        List<Equipment> equipmentList = entityManager.createQuery("SELECT e FROM Equipment e", Equipment.class).getResultList();

        // filtro la lista di equipment in base ai parametri passati
        if (name != null && !name.isEmpty() && !name.isBlank() && availability != null) {

            // se vengono passati entrambi i parametri filtro per entrambi
            equipmentList = equipmentList.stream()
                    .filter(e -> e.getName().equalsIgnoreCase(name) && e.getAvailability().equals(availability))
                    .collect(Collectors.toList());

        }
        if (name != null && !name.isEmpty() && !name.isBlank()) {
            // se viene passato solo il name filtro per il name
            equipmentList = equipmentList.stream()
                    .filter(e -> e.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
        }

        if (availability != null) {

            // se viene passato solo l'availability filtro per l'availability
            equipmentList = equipmentList.stream()
                    .filter(e -> e.getAvailability().equals(availability))
                    .collect(Collectors.toList());
        }

        return equipmentList;
    }


    // recupero l'equipment dal db in base all'id passato
    public Equipment findById(Long id) {
        Equipment equipment = entityManager.find(Equipment.class, id);
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment with id " + id + " not found");
        }
        return equipment;
    }

    // salvo l'equipment nel db
    @Transactional
    public Equipment save(Equipment equipment) {
        entityManager.persist(equipment);
        return equipment;
    }

    // aggiorno l'equipment nel db
    @Transactional
    public Equipment update(Long id, Equipment equipment) {

        // controllo se l'equipment esiste
        Equipment existingEquipment = findById(id);

        // dopo aver recuperato l'equipment, aggiorno i dati
        existingEquipment.setName(equipment.getName());
        existingEquipment.setDescription(equipment.getDescription());
        existingEquipment.setAvailability(equipment.getAvailability());
        existingEquipment.setPurchaseDate(equipment.getPurchaseDate());

        //effettuo il merge sull' equipment esistente
        return entityManager.merge(existingEquipment);
    }

    // elimino l'equipment dal db in base all'id passato
    @Transactional
    public void deleteById(Long id) {
        Equipment equipment = findById(id);
        if (equipment != null) {
            entityManager.remove(equipment);
        }
    }
}
