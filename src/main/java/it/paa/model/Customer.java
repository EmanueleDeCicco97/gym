package it.paa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.paa.validation.ValidSubscription;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "customers")
@ValidSubscription
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "name cannot be empty")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "surname cannot be empty")
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull(message = "date of birth cannot be empty")
    @Column(name = "date_of_birth", nullable = false)
    @Past(message = "date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @NotBlank(message = "gender cannot be empty")
    private String gender;

    @Column(name = "active_subscription")
    @NotEmpty(message = "active subscription cannot be empty")
    private String activeSubscription;

    @OneToMany(mappedBy = "associatedCustomer")
    @JsonIgnore
    private List<TrainingProgram> trainingPrograms;

    public Customer() {
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getActiveSubscription() {
        return activeSubscription;
    }

    public void setActiveSubscription(String activeSubscription) {
        this.activeSubscription = activeSubscription;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<TrainingProgram> getTrainingPrograms() {
        return trainingPrograms;
    }

    public void setTrainingPrograms(List<TrainingProgram> trainingPrograms) {
        this.trainingPrograms = trainingPrograms;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", activeSubscription=" + activeSubscription +
                '}';
    }
}
