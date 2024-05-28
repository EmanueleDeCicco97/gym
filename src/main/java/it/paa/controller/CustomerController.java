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
            return Response.status(Response.Status.NO_CONTENT).type(MediaType.TEXT_PLAIN).build();
        }
        return Response.ok(customers).build();
    }

    @GET
    @Path("/customer_id/{id}")
    public Response getCustomerById(@PathParam("id") Long id) {
        try {
            Customer customer = customerService.findById(id);
            return Response.ok(customer).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response createCustomer(@Valid Customer customer) {
        if (customer == null) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("customer cannot be null").build();
        }
        Customer savedCustomer = customerService.save(customer);
        return Response.status(Response.Status.CREATED).entity(savedCustomer).build();
    }

    @PUT
    @Path("/customer_id/{id}")
    public Response updateCustomer(@PathParam("id") Long id, @Valid Customer customerDetails) {
        if (customerDetails == null) {
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("customer cannot be null").build();
        }

        try {
            Customer updatedCustomer = customerService.update(id, customerDetails);
            return Response.ok(updatedCustomer).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/customer_id/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        try {
            if (trainingProgramService.isCustomerAssociated(id)) {
                throw new IllegalArgumentException("Customer with id " + id + " is associated with a training program and cannot be deleted");
            }

            customerService.deleteById(id);

            return Response.ok().build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();

        }

    }
}
