package it.paa.model;

import it.paa.validation.ValidTrainingIntensity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;


@Entity
@Table(name = "training_program")
@ValidTrainingIntensity
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trainingType;

    @Positive(message = "duration cannot be negative")
    private int duration;

    private String intensity;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer associatedCustomer;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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
