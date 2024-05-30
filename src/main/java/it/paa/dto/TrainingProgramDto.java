package it.paa.dto;

import it.paa.validation.ValidIntensity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TrainingProgramDto {

    @NotBlank(message = "training type cannot be empty")
    private String trainingType;

    @Positive(message = "The duration must be positive.")
    private Integer duration;

    @NotBlank(message = "training type cannot be empty")
    @ValidIntensity
    private String intensity;

    @NotNull(message = "customer id cannot be empty")
    private Long customerId;

    @NotNull(message = "trainer id cannot be empty")
    private Long trainerId;

    public TrainingProgramDto() {

    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    @Override
    public String toString() {
        return "TrainingProgramDto{" +
                "trainingType='" + trainingType + '\'' +
                ", duration=" + duration +
                ", intensity='" + intensity + '\'' +
                ", customerId=" + customerId +
                ", trainerId=" + trainerId +
                '}';
    }
}
