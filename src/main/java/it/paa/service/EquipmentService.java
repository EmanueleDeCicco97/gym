package it.paa.service;

import it.paa.model.Equipment;
import it.paa.repository.EquipmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EquipmentService {
    @Inject
    EquipmentRepository equipmentRepository;


    public List<Equipment> findAll(String name, Integer availability) {

        return equipmentRepository.findAll(name, availability);
    }

    public Equipment findById(Long id) {
        Equipment equipment = equipmentRepository.findById(id);
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment with id " + id + " not found");
        }
        return equipment;
    }

    @Transactional
    public Equipment save(Equipment equipment) {
        equipmentRepository.save(equipment);
        return equipment;
    }


    // aggiorno l'equipment nel db
    @Transactional
    public Equipment update(Long id, Equipment equipment) {

        // controllo se l'equipment esiste
        Equipment existingEquipment = findById(id);

        // dopo aver recuperato l'equipment, aggiorno i dati
        // se il name non è vuoto aggiorno il name
        if (!equipment.getName().isEmpty() && !equipment.getName().isBlank()) {
            existingEquipment.setName(equipment.getName());
        }

        // se la descrizione non è vuota aggiorno la descrizione
        if (!equipment.getDescription().isEmpty() && !equipment.getDescription().isBlank()) {
            existingEquipment.setDescription(equipment.getDescription());
        }
        // se l'availability non è null aggiorno l'availability
        if (equipment.getAvailability() != null) {
            existingEquipment.setAvailability(equipment.getAvailability());
        }
        if (equipment.getPurchaseDate() != null) {
            existingEquipment.setPurchaseDate(equipment.getPurchaseDate());
        }

        equipmentRepository.update(existingEquipment);
        //effettuo il merge sull' equipment esistente
        return existingEquipment;
    }

    @Transactional
    public Equipment deleteById(Long id) {
        Equipment equipment = equipmentRepository.deleteById(id);
        if (equipment != null) {
            return equipment;
        } else {
            throw new IllegalArgumentException("Equipment with id " + id + " not found");
        }
    }

}
