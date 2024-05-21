package it.paa.controller;

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
    public List<TrainingProgram> findAll(@QueryParam("duration") Integer duration,
                                         @QueryParam("intensity") String intensity) {
        return trainingProgramService.findAll(duration, intensity);
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            TrainingProgram trainingProgram = trainingProgramService.findById(id);

            return Response.ok(trainingProgram).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{customerId}/{trainerId}")
    public Response save(@PathParam("customerId") Long customerId,
                         @PathParam("trainerId") Long trainerId,
                         @Valid TrainingProgram trainingProgram) {
        try {

            // cerco nel db il customer e il trainer in base all'id
            Customer customer = customerService.findById(customerId);
            Trainer trainer = trainerService.findById(trainerId);

            // controllo se il cliente è già associato a questo trainer
            boolean isAlreadyAssociated = trainingProgramService.isCustomerAssociated(customerId);
            if (isAlreadyAssociated) {
                return Response.status(Response.Status.CONFLICT)
                        .type(MediaType.TEXT_PLAIN)
                        .entity("The client is already associated with a trainer.")
                        .build();
            }

            // setto il customer e il trainer nel training program
            trainingProgram.setAssociatedCustomer(customer);
            trainingProgram.setAssociatedTrainer(trainer);

            TrainingProgram createdTrainingProgram = trainingProgramService.save(trainingProgram);
            return Response.status(Response.Status.CREATED).entity(createdTrainingProgram).build();
        } catch (IllegalArgumentException e) {

            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid TrainingProgram trainingProgram) {
        try {
            TrainingProgram updatedTrainingProgram = trainingProgramService.update(id, trainingProgram);
            return Response.ok(updatedTrainingProgram).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id) {
        try {
            trainingProgramService.deleteById(id);
            return Response.noContent().build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{trainingType}")
    public List<TrainingProgram> getCustomersByTrainingType(@PathParam("trainingType") String trainingType) {
        return trainingProgramService.findByTrainingType(trainingType);
    }


}
