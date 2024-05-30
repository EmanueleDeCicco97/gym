package it.paa.controller;

import it.paa.util.ErrorMessage;
import it.paa.model.Trainer;
import it.paa.service.TrainerService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/trainers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrainerController {

    @Inject
    TrainerService trainerService;

    @GET
    public Response getAllTrainers(@QueryParam("name") String name, @QueryParam("specialization") String specialization) {
        List<Trainer> trainerList = trainerService.findAll(name, specialization);
        if (trainerList.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .build();
        }
        return Response.ok(trainerList).build();
    }

    @GET
    @Path("/{id}")
    public Response getTrainerById(@PathParam("id") Long id) {
        try {
            Trainer trainer = trainerService.findById(id);
            return Response.ok(trainer).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage(e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response createTrainer(@Valid Trainer trainer) {

        Trainer savedTrainer = trainerService.save(trainer);
        return Response.status(Response.Status.CREATED).entity(savedTrainer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTrainer(@PathParam("id") Long id, @Valid Trainer trainerDetails) {

        try {
            Trainer updatedTrainer = trainerService.update(id, trainerDetails);
            return Response.ok(updatedTrainer).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTrainer(@PathParam("id") Long id) {
        try {
            trainerService.deleteById(id);
            return Response.ok().build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/most_requested_trainer")
    public Response findTopTrainerWithClients() {
        return Response.ok(trainerService.findTopTrainerWithMaxClients()).build();
    }
}
