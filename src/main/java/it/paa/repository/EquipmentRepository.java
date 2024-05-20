package it.paa.repository;

import it.paa.model.Equipment;

import java.util.List;

public interface EquipmentRepository {

    List<Equipment> findAll(String name, Integer availability);

    Equipment findById(Long id);

    Equipment save(Equipment equipment);

    Equipment update(Long id, Equipment equipment);

    void deleteById(Long id);
}
