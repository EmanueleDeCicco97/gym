package it.paa.controller;

import it.paa.model.Equipment;
import it.paa.service.EquipmentService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/equipments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EquipmentController {

    @Inject
    EquipmentService equipmentService;

    @GET
    public Response getAllEquipment(@QueryParam("name") String name, @QueryParam("availability") Integer availability) {
        List<Equipment> equipmentList = equipmentService.findAll(name, availability);
        return Response.ok(equipmentList).build();
    }

    @GET
    @Path("/{id}")
    public Response getEquipmentById(@PathParam("id") Long id) {
        try {
            Equipment equipment = equipmentService.findById(id);
            return Response.ok(equipment).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    public Response createEquipment(@Valid Equipment equipment) {
        Equipment savedEquipment = equipmentService.save(equipment);
        return Response.status(Response.Status.CREATED).entity(savedEquipment).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateEquipment(@PathParam("id") Long id, @Valid Equipment equipmentDetails) {
        equipmentDetails.setId(id);
        try {
            Equipment updatedEquipment = equipmentService.update(id, equipmentDetails);
            return Response.ok(updatedEquipment).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEquipment(@PathParam("id") Long id) {
        try {
            equipmentService.deleteById(id);
            return Response.ok().build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }
}
