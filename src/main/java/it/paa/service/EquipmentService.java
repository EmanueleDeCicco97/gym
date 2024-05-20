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
        List<Equipment> equipmentList = entityManager.createQuery("SELECT e FROM Equipment e", Equipment.class).getResultList();
        if (name != null && !name.isEmpty() && !name.isBlank() && availability != null) {
            equipmentList = equipmentList.stream()
                    .filter(e -> e.getName().equalsIgnoreCase(name) && e.getAvailability().equals(availability))
                    .collect(Collectors.toList());

        }
        if (name != null && !name.isEmpty() && !name.isBlank()) {
            equipmentList = equipmentList.stream()
                    .filter(e -> e.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
        }

        if (availability != null) {
            equipmentList = equipmentList.stream()
                    .filter(e -> e.getAvailability().equals(availability))
                    .collect(Collectors.toList());
        }

        return equipmentList;
    }


    public Equipment findById(Long id) {
        Equipment equipment = entityManager.find(Equipment.class, id);
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment with id " + id + " not found");
        }
        return equipment;
    }

    @Transactional
    public Equipment save(Equipment equipment) {
        entityManager.persist(equipment);
        return equipment;
    }

    @Transactional
    public Equipment update(Long id, Equipment equipment) {

        Equipment existingEquipment = findById(id);

        existingEquipment.setName(equipment.getName());
        existingEquipment.setDescription(equipment.getDescription());
        existingEquipment.setAvailability(equipment.getAvailability());
        existingEquipment.setPurchaseDate(equipment.getPurchaseDate());

        return entityManager.merge(existingEquipment);
    }

    @Transactional
    public void deleteById(Long id) {
        Equipment equipment = findById(id);
        if (equipment != null) {
            entityManager.remove(equipment);
        }
    }
}
