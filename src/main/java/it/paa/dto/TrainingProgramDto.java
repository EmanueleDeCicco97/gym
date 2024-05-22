package it.paa.dto;

import jakarta.validation.constraints.Positive;

public class TrainingProgramDto {

    private String trainingType;
    @Positive(message = "The duration must be positive.")
    private Integer duration;

    private String intensity;
    private Long customerId;
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
