package it.paa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.paa.validation.ValidIntensity;
import it.paa.validation.ValidTrainingIntensity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@Entity
@Table(name = "training_programs")
@ValidTrainingIntensity
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "training_type")
    @NotEmpty(message = "training type cannot be empty")
    private String trainingType;

    @Positive(message = "duration cannot be negative")
    @NotNull(message = "duration cannot be null")
    private Integer duration;

    @NotBlank(message = "intensity cannot be empty")
    @ValidIntensity
    private String intensity;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Customer associatedCustomer;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Trainer associatedTrainer;

    public TrainingProgram() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Customer getAssociatedCustomer() {
        return associatedCustomer;
    }

    public void setAssociatedCustomer(Customer associatedCustomer) {
        this.associatedCustomer = associatedCustomer;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public Trainer getAssociatedTrainer() {
        return associatedTrainer;
    }

    public void setAssociatedTrainer(Trainer associatedTrainer) {
        this.associatedTrainer = associatedTrainer;
    }

    @Override
    public String toString() {
        return "TrainingProgram{" +
                "id=" + id +
                ", trainingType='" + trainingType + '\'' +
                ", duration=" + duration +
                ", intensity='" + intensity + '\'' +
                '}';
    }
}
