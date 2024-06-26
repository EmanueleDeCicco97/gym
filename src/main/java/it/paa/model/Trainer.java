package it.paa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotBlank(message = "Specialization cannot be empty")
    @Column(name = "specialization", nullable = false)
    private String specialization;

    @OneToMany(mappedBy = "associatedTrainer")
    @JsonIgnore
    private List<TrainingProgram> trainingPrograms;

    @Column(name = "work_hours")
    @NotNull(message = "workHours cannot be null")
    @Positive(message = "workHours must be greater than 0")
    private Integer workHours;

    public Trainer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<TrainingProgram> getTrainingPrograms() {
        return trainingPrograms;
    }

    public void setTrainingPrograms(List<TrainingProgram> trainingPrograms) {
        this.trainingPrograms = trainingPrograms;
    }

    public Integer getWorkHours() {
        return workHours;
    }

    public void setWorkHours(Integer workHours) {
        this.workHours = workHours;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "workHours=" + workHours +
                ", specialization='" + specialization + '\'' +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
