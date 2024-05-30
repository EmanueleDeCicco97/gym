package it.paa.controller;

import it.paa.service.CustomerService;
import it.paa.service.TrainingProgramService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import it.paa.model.Customer;

import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    @Inject
    CustomerService customerService;
    @Inject
    TrainingProgramService trainingProgramService;

    @GET
    public Response getAllCustomers(@QueryParam("name") String name, @QueryParam("gender") String gender) {
        List<Customer> customers = customerService.findAll(name, gender);
        if (customers.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(customers).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") Long id) {
        try {
            Customer customer = customerService.findById(id);
            return Response.ok(customer).build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response createCustomer(@Valid Customer customer) {

        Customer savedCustomer = customerService.save(customer);
        return Response.status(Response.Status.CREATED).entity(savedCustomer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, @Valid Customer customerDetails) {

        try {
            Customer updatedCustomer = customerService.update(id, customerDetails);
            return Response.ok(updatedCustomer).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        try {
            if (trainingProgramService.isCustomerAssociated(id)) {
                throw new NotFoundException("Customer with id " + id + " is associated with a training program and cannot be deleted");
            }

            customerService.deleteById(id);

            return Response.ok().build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();

        }

    }
}
