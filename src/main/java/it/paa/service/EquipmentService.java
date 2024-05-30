package it.paa.service;

import it.paa.model.Equipment;
import it.paa.repository.EquipmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

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
            throw new NotFoundException("Equipment with id " + id + " not found");
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

        existingEquipment.setName(equipment.getName());

        existingEquipment.setDescription(equipment.getDescription());

        existingEquipment.setAvailability(equipment.getAvailability());

        existingEquipment.setPurchaseDate(equipment.getPurchaseDate());

        //effettuo il merge sull' equipment esistente
        equipmentRepository.update(existingEquipment);

        return existingEquipment;
    }

    @Transactional
    public Equipment deleteById(Long id) {
        Equipment equipment = equipmentRepository.deleteById(id);
        if (equipment != null) {
            return equipment;
        } else {
            throw new NotFoundException("Equipment with id " + id + " not found");
        }
    }

}
