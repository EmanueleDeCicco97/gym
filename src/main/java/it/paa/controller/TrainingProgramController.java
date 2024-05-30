package it.paa.controller;

import io.quarkus.arc.ArcUndeclaredThrowableException;
import it.paa.util.ErrorMessage;
import it.paa.dto.TrainingProgramDto;
import it.paa.model.Customer;
import it.paa.model.Trainer;
import it.paa.model.TrainingProgram;
import it.paa.service.CustomerService;
import it.paa.service.TrainerService;
import it.paa.service.TrainingProgramService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/training-programs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TrainingProgramController {

    @Inject
    TrainingProgramService trainingProgramService;
    @Inject
    CustomerService customerService;
    @Inject
    TrainerService trainerService;

    @GET
    public Response findAll(@QueryParam("duration") Integer duration,
                            @QueryParam("intensity") String intensity) {
        List<TrainingProgram> trainingPrograms = trainingProgramService.findAll(duration, intensity);
        if (trainingPrograms.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(trainingPrograms).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            TrainingProgram trainingProgram = trainingProgramService.findById(id);

            return Response.ok(trainingProgram).build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/trainer_id/{trainerId}/customer/{customerId}")
    public Response save(@PathParam("trainerId") Long trainerId,
                         @PathParam("customerId") Long customerId,
                         @Valid TrainingProgram trainingProgram) {
        try {

            // cerco nel db il customer e il trainer in base all'id
            Customer customer = customerService.findById(customerId);
            Trainer trainer = trainerService.findById(trainerId);

            // controllo se il cliente è già associato a questo trainer
            boolean isAlreadyAssociated = trainingProgramService.isCustomerAssociated(customerId);
            if (isAlreadyAssociated) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorMessage("The client is already associated with a trainer."))
                        .build();
            }

            // setto il customer e il trainer nel training program
            if (customer != null) {
                trainingProgram.setAssociatedCustomer(customer);
            }
            if (trainer != null) {
                trainingProgram.setAssociatedTrainer(trainer);
            }

            TrainingProgram createdTrainingProgram = trainingProgramService.save(trainingProgram);
            return Response.status(Response.Status.CREATED).entity(createdTrainingProgram).build();

        } catch (NotFoundException e) {

            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessage(e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid TrainingProgramDto trainingProgramDto) {
        try {

            TrainingProgram updatedTrainingProgram = trainingProgramService.update(id, trainingProgramDto);
            return Response.ok(updatedTrainingProgram).build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessage(e.getMessage())).build();

        } catch (ArcUndeclaredThrowableException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("the intensity can only be easy, medium and hard")
                    .build();

        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id) {
        try {
            trainingProgramService.deleteById(id);
            return Response.ok().build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessage(e.getMessage())).build();
        }
    }

    @GET
    @Path("training_type/{trainingType}")
    public List<TrainingProgram> getCustomersByTrainingType(@PathParam("trainingType") String trainingType) {
        return trainingProgramService.findByTrainingType(trainingType);
    }

}
